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

#  — Entrada, Saída, Objetivo e Restrições

## Objetivo

Determinar a **velocidade máxima total** (taxa máxima de transferência de dados) com que os dados conseguem sair do servidor (computador $1$) e chegar até o computador de Kotivalo (computador $n$), combinando os caminhos da rede sem ultrapassar a capacidade de nenhuma conexão.

---

## Formato de Entrada (*Input*)

* **Primeira linha:** Contém dois números inteiros $n$ e $m$, representando, respectivamente, a quantidade de computadores e a quantidade de conexões na rede.
  * Os computadores são numerados de $1$ a $n$.
  * O computador $1$ representa o **servidor** (origem).
  * O computador $n$ representa o **computador de Kotivalo** (destino).
* **Próximas $m$ linhas:** Cada linha contém três números inteiros $a$, $b$ e $c$, indicando que existe uma conexão que permite ao computador $a$ enviar dados para o computador $b$ a uma velocidade máxima $c$.

---

## Formato de Saída (*Output*)

* Imprimir um **único número inteiro** representando a velocidade máxima total com que Kotivalo consegue baixar os dados do servidor.

---

## Restrições (*Constraints*) e Impactos na Modelagem

| Parâmetro | Restrição | Impacto na Solução |
| :--- | :--- | :--- |
| Computadores ($n$) | $1 \le n \le 500$ | Permite algoritmos polinomiais como $O(n \cdot m^2)$ com execução instantânea. |
| Conexões ($m$) | $1 \le m \le 1000$ | Grafo esparso a moderado; ideal para representação via lista de adjacência. |
| Extremidades ($a, b$) | $1 \le a, b \le n$ | Garante que todas as conexões ligam computadores válidos da rede. |
| Velocidade ($c$) | $1 \le c \le 10^9$ | A soma do fluxo total pode ultrapassar o limite de inteiros de $32$ bits ($\approx 2 \cdot 10^9$), exigindo variáveis de **$64$ bits** (`long long`) e um algoritmo cujo tempo não dependa do valor de $c$. |

---

## Exemplo de Entrada e Saída

**Entrada:**
```text
4 5
1 2 3
2 4 2
1 3 4
3 4 5
4 1 3
```

**Saída:**
```text
6
```

# — Definição de Vértices, Arestas, Direção e Capacidades

A rede de computadores do problema é modelada formalmente como um grafo direcionado e ponderado $G = (V, E)$, onde cada elemento possui um papel específico:

## Elementos do Grafo

| Elemento Matemático | Representação no Problema | Detalhamento |
| :--- | :--- | :--- |
| **Vértices ($V$)** | Os $n$ computadores da rede | $V = \{1, 2, \dots, n\}$. O vértice $1$ é a **fonte** (*source*, o servidor) e o vértice $n$ é o **sumidouro** (*sink*, o computador de Kotivalo). Os demais vértices apenas repassam os dados sem armazená-los. |
| **Arestas ($E$)** | As $m$ conexões de rede | Cada par $(a, b) \in E$ representa um canal de comunicação entre o computador $a$ e o computador $b$. |
| **Direção** | **Grafo Direcionado** (Dígrafo) | As conexões possuem sentido único: a aresta $(a, b)$ permite tráfego apenas de $a$ para $b$, e não no sentido inverso (a menos que exista outra aresta explícita $(b, a)$). |
| **Pesos / Capacidades ($c$)** | Velocidade máxima da conexão | Cada aresta $(a, b)$ recebe um peso inteiro $c(a, b) \ge 1$ que define o limite máximo de dados por segundo que pode atravessar aquele canal. |

---

## Mapeamento da Instância de Exemplo ($n = 4, m = 5$)

* **Conjunto de Vértices:** $V = \{1, 2, 3, 4\}$
  * Origem (Servidor): $1$
  * Destino (Kotivalo): $4$
* **Conjunto de Arestas Direcionadas e Capacidades:**
  1. $(1 \rightarrow 2)$ com capacidade $c = 3$
  2. $(2 \rightarrow 4)$ com capacidade $c = 2$
  3. $(1 \rightarrow 3)$ com capacidade $c = 4$
  4. $(3 \rightarrow 4)$ com capacidade $c = 5$
  5. $(4 \rightarrow 1)$ com capacidade $c = 3$ *(aresta de retorno, sem utilidade para enviar dados até o destino $4$)*

  # — Identificação Preliminar da Família do Problema

## Classificação

O problema pertence à família de **Fluxo Máximo** (*Maximum Flow*).

---

## Justificativa da Classificação

O objetivo central do problema é fazer chegar o maior volume de dados possível da origem (servidor, vértice $1$) até o destino (computador de Kotivalo, vértice $n$), combinando múltiplas rotas simultâneas da rede e respeitando rigorosamente o limite de velocidade (capacidade) de cada aresta sem sobrecarregar nenhuma conexão.

---

## Comparação com Outras Famílias de Problemas em Grafos

| Família de Problemas | Objetivo Principal | Por que se aplica ou não ao problema? |
| :--- | :--- | :--- |
| **Árvore Geradora Mínima (MST)** | Conectar todos os vértices de um grafo não direcionado usando o menor custo total possível nas arestas. | **Não se aplica.** Não queremos interligar todos os computadores pelo menor custo, mas sim maximizar a taxa de envio entre dois pontos específicos ($1$ e $n$). |
| **Caminho Mínimo** | Encontrar uma única rota de menor distância ou menor custo entre uma origem e um destino. | **Não se aplica.** Escolher apenas um único caminho mais curto subutilizaria a rede, pois ignoraria a possibilidade de somar o tráfego de várias rotas operando em paralelo. |
| **Fluxo Máximo** | Maximizar a taxa total de transporte entre uma fonte e um sumidouro respeitando as capacidades individuais das arestas direcionadas. | **Aplica-se perfeitamente.** Captura a existência de origem, destino, arestas direcionadas com capacidades máximas e a soma de múltiplos caminhos simultâneos até esgotar os gargalos da rede. |