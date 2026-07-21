# RealmComposeUI — Contexto Arquitetural

Projeto de estudo de **Server Driven UI (SDUI)** com Jetpack Compose. O backend é um
`json-server` local servindo `db.json`; o app **não conhece telas**, só sabe renderizar
componentes descritos pelo servidor.

- Pacote raiz (`<pkg>`): `br.com.claudiosoaresdev.realmcomposeui`
- Prefixo de convention plugins (`<P>` / `<p>`): `Realm` / `realm`
- Módulos: `:app`, `:core`, `:feature` — sem submódulos — mais o included build
  `build-logic`.
- `compileSdk` 37.1 / `minSdk` 24 / `targetSdk` 36 / Java 11 — definidos em `RealmBuild`.
- Fonte Kotlin em `src/<variant>/kotlin` (registrado pelo convention plugin).

## Backend de estudo

```bash
npx json-server db.json          # http://localhost:3000
```

Endpoints:

| Rota | Uso |
| --- | --- |
| `GET /appConfig` | bootstrap: `defaultScreen`, `defaultHouse`, `supportedSchemaVersions`, `supportedLayouts` |
| `GET /sdui?screen=house_home&house=targaryen` | payload da tela |
| `GET /sdui/:id` | payload por id (`house-home-targaryen`) |

Base URL em `RealmNetwork.BASE_URL` (`http://10.0.2.2:3000/` — o host da máquina visto
de dentro do emulador).

## Contrato SDUI

```
sdui[] → { id, screen, house, version, schemaVersion, layout, payload }
payload → { analytics, theme, screen, components[] }
component → { id, type, visible, props }
```

- **`schemaVersion` / `layout`**: negociados contra `appConfig`. Payload fora do suportado
  não renderiza — cai em fallback, nunca em crash.
- **`theme`**: `background`, `colors`, `typography`, `shapes`, `spacing`. Todo token visual
  vem do servidor; `core:designsystem` só define o *shape* dos tokens e o default de
  fallback, jamais a paleta de uma casa.
- **`screen`**: `orientation`, `scrollable`, `contentPadding`, `componentSpacingDp`.
- **`components[]`**: lista ordenada. `type` conhecidos hoje — `topBar`, `heroBanner`,
  `iconGrid`, `horizontalCarousel`, `newsList`, `bottomNavigation`.
- **`action`**: `{ type, route?, enabled }`. Tipos: `navigate`, `open_drawer`. Toda ação é
  dado, nunca código — o interpretador mapeia `type` → handler registrado.
- **`icon`**: string simbólica (`menu`, `crown`, `dragon_egg`, `tower`…) resolvida por um
  registry local. Ícone desconhecido → placeholder, não exceção.

### Invariantes de renderização

1. `type` desconhecido → componente é **ignorado com log**, o resto da tela renderiza.
2. Campo opcional ausente → default do design system. Campo obrigatório ausente → o
   componente é descartado, não a tela.
3. `enabled: false` desabilita interação, mas o item continua visível.
4. `visible: false` remove o componente da composição.
5. Parsing do payload é **puro e testável** fora do Compose: JSON → modelo de domínio →
   `UiComponent` selado. O composable recebe modelo tipado, nunca `JsonElement`.

## Estrutura

Três módulos Gradle, **nenhum submódulo**: `:app`, `:core`, `:feature` (+ o included build).

```
<root>/
├── db.json                        # base do json-server
├── build.gradle.kts               # só plugins alias(...) apply false
├── settings.gradle.kts            # pluginManagement { includeBuild("build-logic") }
├── gradle/libs.versions.toml      # fonte única de versões
├── build-logic/                   # included build, módulo lib único
│   ├── settings.gradle.kts        # catálogo `libs` importado de ../gradle
│   ├── build.gradle.kts           # `kotlin-dsl` + gradlePlugin { register(...) }
│   └── src/main/kotlin/<pkg>/buildlogic/
│       ├── helper/RealmConventionHelpers.kt   # RealmBuild + configureAndroidCommon
│       └── convention/                        # realm.android.{application,library,compose,
│                                              #   network,di,testing} e realm.kotlin.library
├── core/                          # infra e transversais; zero regra de feature
│   └── src/main/kotlin/<pkg>/core/
│       ├── model/          SduiModels.kt — contrato tipado, sem tipo de framework
│       ├── parser/         SduiPayloadParser.kt — mapa cru → modelo (puro, testado)
│       ├── network/        SduiApi.kt (Retrofit) + RealmNetwork.BASE_URL
│       ├── repository/     SduiRepository — valida schema/layout contra o appConfig
│       ├── designsystem/   RealmSduiTheme (tema do payload) + registry de ícones
│       └── di/             coreModule
├── feature/                       # vertical slice de UI
│   └── src/main/kotlin/<pkg>/feature/
│       ├── navigation/     RealmRoute (rota SDUI → destino) + RealmNavHost
│       ├── di/             featureModule
│       └── presentation/
│           ├── host/       RealmApp (Scaffold + bottom bar) + SduiHostViewModel
│           ├── components/ um composable por `type` do payload
│           ├── home/       HomeScreen — interpretador da lista de componentes
│           ├── houses/     troca de casa → recarrega payload
│           └── map/ decrees/ profile/   destinos ainda sem payload
└── app/                           # único consumidor: Application, Activity, grafo Koin
```

## Navegação

Cinco destinos: `home`, `houses`, `map`, `decrees`, `profile`.

- O `NavHost` vive em `:feature`; `:app` só chama `RealmApp()`.
- A **barra inferior é SDUI**: rótulo, ícone, ordem e habilitação vêm do componente
  `bottomNavigation` do payload. Sem payload, cai em `fallbackItems()` só para o app
  continuar navegável.
- Rota do servidor → destino local em `RealmRoute.fromSduiRoute`. Rota sem destino
  (`/characters/...`) devolve `null` e vira snackbar — nunca navegação errada.
- `/sdui?house=x` e `/house/x` também trocam a casa ativa (`houseFromSduiRoute`),
  o que recarrega tema, textos e barra inferior de uma vez.
- O tema é global: aplicado no `RealmApp`, vale para todos os destinos — inclusive os
  que não vêm de payload.

## Regras de dependência

```
app      → feature, core
feature  → core            (api, para expor o modelo ao app)
core    ↛ feature
```

Ao dividir `core`/`feature` em módulos menores, o alvo é o do NowInAndroid:
`feature:*:domain` JVM puro, `feature:A ↛ feature:B`, `core:designsystem ↛ core:ui`,
com validação automática por task de build.

## Convenções

- Módulo declara **o que é**, não como se configura: nenhuma configuração de SDK, Java,
  flavor ou buildType fora de `build-logic`. O `build.gradle.kts` do `:app` só declara
  `plugins { id("realm.*") }`, `namespace`, `applicationId` e dependências.
- Flavors `dev` / `staging` / `prod` vivem no `RealmAndroidApplicationPlugin`. Variante
  padrão de trabalho: `devDebug`.
- Parse e validação de schema vivem em `:core` e não tocam Compose: `SduiPayloadParser`
  é `object` puro sobre `Map<String, Any?>`, rodando em teste JVM comum.
- O `:app` não conhece componente SDUI nenhum: ele só inicia o Koin e chama `RealmApp()`.
- Emulador fala com o json-server em `http://10.0.2.2:3000/`; cleartext liberado só no
  buildType `debug` (`src/debug/res/xml/network_security_config.xml`).
- Testes espelham o pacote de produção. Fixtures/fakes vivem no `test/` do módulo que
  **define** a interface.
- Toda versão nova entra em `gradle/libs.versions.toml`, na seção comentada correta.
  Marcador `# [BASE]` = veio do template original do Android Studio.
- DI é Koin; ViewModel por tela expõe `State` e consome `Event`.

## Comandos

```bash
npx json-server db.json         # sobe a API de estudo
./gradlew :app:assembleDevDebug
./gradlew :core:testDebugUnitTest :feature:testDebugUnitTest
./gradlew :app:lintDevDebug
```
