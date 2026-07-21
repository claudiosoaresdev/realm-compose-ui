# public/

Estáticos servidos pelo json-server. Os caminhos abaixo são exatamente os que o
`db.json` referencia — nome diferente = imagem não encontrada.

```bash
npx json-server db.json --static ./public
```

`/assets/targaryen/logo.png` → `http://localhost:3000/assets/targaryen/logo.png`
(do emulador: `http://10.0.2.2:3000/...`).

## Arquivos esperados

### targaryen
| Arquivo | Onde aparece |
| --- | --- |
| `logo.png` | `topBar.props.logo` |
| `background-dragon.png` | `theme.background.image` |
| `banner-background.jpg` | `heroBanner.props.backgroundImage` |
| `banner-three-headed-dragon.png` | `heroBanner.props.foregroundImage` |
| `members/viserys-i.jpg` | carrossel de membros |
| `members/rhaenyra.jpg` | carrossel de membros |
| `members/daemon.jpg` | carrossel de membros |
| `members/rhaenys.jpg` | carrossel de membros |
| `news/dragons-over-kings-landing.jpg` | lista de notícias |
| `news/dragonstone-defenses.jpg` | lista de notícias |
| `news/black-council.jpg` | lista de notícias |

### hightower
| Arquivo | Onde aparece |
| --- | --- |
| `logo.png` | `topBar.props.logo` |
| `background-tower.png` | `theme.background.image` |
| `banner-background.jpg` | `heroBanner.props.backgroundImage` |
| `banner-tower-emblem.png` | `heroBanner.props.foregroundImage` |
| `members/otto-hightower.jpg` | carrossel de membros |
| `members/alicent-hightower.jpg` | carrossel de membros |
| `members/gwayne-hightower.jpg` | carrossel de membros |
| `members/hobert-hightower.jpg` | carrossel de membros |
| `news/citadel-archives.jpg` | lista de notícias |
| `news/oldtown-alliance.jpg` | lista de notícias |
| `news/hightower-host.jpg` | lista de notícias |

Arquivo ausente não quebra a tela: o componente cai no placeholder do design system.
