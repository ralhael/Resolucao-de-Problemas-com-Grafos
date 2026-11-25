# Tópico 1 — Resumo do Problema em Linguagem Própria

## Descrição Geral

O problema **Download Speed** modela uma rede de computadores interligados por conexões de comunicação com limites específicos de velocidade. Nessa rede, existe um servidor principal (identificado como computador $1$) que precisa enviar arquivos para o computador de destino pertencente a Kotivalo (identificado como computador $n$).

O objetivo do problema **não é encontrar apenas um único caminho mais rápido** entre o servidor e o destino, mas sim aproveitar todas as rotas disponíveis da rede trabalhando ao mesmo tempo para fazer chegar a **maior velocidade total de dados** até o computador de Kotivalo, respeitando o limite máximo de cada conexão sem sobrecarregar nenhum cabo.

---

## Ideia Central da Solução

* **Múltiplos caminhos simultâneos:** Os dados podem se dividir por diferentes rotas intermediárias da rede para chegar ao destino.
* **Gargalos:** Em cada rota escolhida, a velocidade que consegue atravessar aquele caminho é limitada pela conexão mais lenta do trajeto (o gargalo).
* **Capacidade respeitada:** A soma dos dados que passam por qualquer conexão nunca pode ultrapassar a sua velocidade máxima suportada.

---

## Ilustração com o Exemplo Base

Na entrada de exemplo com $n = 4$ computadores e $m = 5$ conexões:

* Pela rota $1 \rightarrow 2 \rightarrow 4$, as velocidades são $3$ e $2$, então o gargalo permite passar **$2$** unidades de fluxo.
* Pela rota $1 \rightarrow 3 \rightarrow 4$, as velocidades são $4$ e $5$, então o gargalo permite passar **$4$** unidades de fluxo.
* A conexão $4 \rightarrow 1$ (velocidade $3$) está no sentido contrário ao destino e não contribui para o download.

Somando as duas rotas simultâneas, a velocidade máxima total que chega ao computador de Kotivalo é **$2 + 4 = 6$**.