# Marco 2 — Escolha e Justificativa do Algoritmo

## Algoritmo Escolhido

Para resolver o problema **Download Speed**, adotamos o método de **Ford-Fulkerson**, implementado por meio da especialização de **Edmonds-Karp** (que utiliza Busca em Largura — BFS — para selecionar os caminhos).

---

## Justificativa Conforme a Classe do Problema

Como o problema pertence à família de **Fluxo Máximo**, a escolha do algoritmo baseia-se diretamente no objetivo de maximizar a taxa de transferência entre o servidor ($1$) e o computador de Kotivalo ($n$) sem ultrapassar o limite de nenhuma conexão:

| Algoritmo(s) | Família Original | Por que usar ou descartar neste problema? |
| :--- | :--- | :--- |
| **Kruskal e Prim** | Árvore Geradora Mínima (MST) | **Descartados.** Conectam todos os vértices da rede pelo menor custo total, sem noção de origem, destino ou capacidades direcionadas de envio. |
| **Dijkstra** | Caminho Mínimo | **Descartado.** Encontra apenas uma única rota de menor custo/distância entre dois pontos, em vez de combinar múltiplas rotas simultâneas para somar vazões. |
| **Ford-Fulkerson / Edmonds-Karp** | **Fluxo Máximo** | **Escolhido.** Calcula o volume máximo que consegue sair da fonte ($1$) e chegar ao sumidouro ($n$), respeitando os limites de capacidade de cada aresta. |

---

## Como o Algoritmo Funciona na Prática

1. **Busca de Caminhos Livres:** O algoritmo procura uma rota disponível da origem ($1$) até o destino ($n$) onde todas as conexões ainda tenham capacidade livre maior que zero.
2. **Identificação do Gargalo:** Ao encontrar um caminho, verifica qual é a menor capacidade entre todas as arestas daquela rota. Esse valor é o **gargalo**, pois limita quanto dado consegue passar pelo trajeto sem estourar nenhum cabo.
3. **Desconto e Grafo Residual:** O valor do gargalo é somado ao fluxo máximo total e **descontado** (subtraído) da capacidade disponível de cada conexão utilizada naquele caminho. Ao mesmo tempo, o algoritmo adiciona esse mesmo valor em arestas reversas no **grafo residual**, permitindo redirecionar fluxos caso uma combinação melhor seja encontrada nas rodadas seguintes.
4. **Por que usar Edmonds-Karp (BFS)?** O Ford-Fulkerson define a mecânica de gargalo e desconto, mas deixa livre a forma de buscar os caminhos. Ao especializá-lo com **Edmonds-Karp** (usando BFS), garantimos que o algoritmo sempre escolha primeiro os caminhos com o **menor número de conexões**, evitando lentidão quando as velocidades atingem valores altos como $c \le 10^9$.

#  — Representação da Instância Pequena

## Dados da Instância de Exemplo

Consideramos a entrada oficial do problema com **$n = 4$** computadores e **$m = 5$** conexões direcionadas:

```text
4 5
1 2 3
2 4 2
1 3 4
3 4 5
4 1 3
```

---

## Mapeamento Estrutural do Grafo

* **Vértices ($V = \{1, 2, 3, 4\}$):**
  * **Vértice $1$:** Servidor (Fonte / Origem).
  * **Vértices $2$ e $3$:** Computadores intermediários de repasse.
  * **Vértice $4$:** Computador de Kotivalo (Sumidouro / Destino).

* **Arestas Direcionadas e Capacidades ($E$):**
  * $e_1: (1 \rightarrow 2)$ com capacidade $c = 3$
  * $e_2: (2 \rightarrow 4)$ com capacidade $c = 2$
  * $e_3: (1 \rightarrow 3)$ com capacidade $c = 4$
  * $e_4: (3 \rightarrow 4)$ com capacidade $c = 5$
  * $e_5: (4 \rightarrow 1)$ com capacidade $c = 3$

---

## Representação Visual da Rede

```text
          (cap. 3)             (cap. 2)
      +--------------> [ 2 ] -------------->+
      |                                     |
      |                                     v
    [ 1 ]                                 [ 4 ]
   (Fonte)                              (Destino)
      |                                     ^
      |                                     |
      +--------------> [ 3 ] -------------->+
          (cap. 4)             (cap. 5)

    * Aresta adicional de retorno: [ 4 ] ---> [ 1 ] (cap. 3)
```

---

## Representação Inicial do Grafo Residual (Lista de Adjacência)

Para executar o **Ford-Fulkerson / Edmonds-Karp**, cada conexão original ganha uma **aresta reversa** iniciada com capacidade $0$, permitindo registrar os descontos e eventuais redirecionamentos de fluxo:

| Vértice | Arestas Originais (Ida) | Arestas Reversas (Volta — inicialmente $0$) |
| :--- | :--- | :--- |
| **$1$** | $(1 \rightarrow 2, \text{cap: } 3)$, $(1 \rightarrow 3, \text{cap: } 4)$ | $(1 \rightarrow 4, \text{cap: } 0)$ |
| **$2$** | $(2 \rightarrow 4, \text{cap: } 2)$ | $(2 \rightarrow 1, \text{cap: } 0)$ |
| **$3$** | $(3 \rightarrow 4, \text{cap: } 5)$ | $(3 \rightarrow 1, \text{cap: } 0)$ |
| **$4$** | $(4 \rightarrow 1, \text{cap: } 3)$ | $(4 \rightarrow 2, \text{cap: } 0)$, $(4 \rightarrow 3, \text{cap: } 0)$ |

#  — Validação da Entrada e Saída Esperada

## Rastreamento Passo a Passo (Edmonds-Karp / BFS)

Partindo do fluxo total acumulado $F = 0$, executamos buscas em largura (BFS) sucessivas a partir do servidor ($1$) até o computador de Kotivalo ($4$):

---

### Iteração 1 — Primeiro Caminho Aumentante

* **Caminho encontrado pela BFS:** $1 \rightarrow 2 \rightarrow 4$
* **Capacidades disponíveis no caminho:**
  * Aresta $(1 \rightarrow 2)$: capacidade $3$
  * Aresta $(2 \rightarrow 4)$: capacidade $2$
* **Identificação do gargalo:** $\min(3, 2) = 2$
* **Atualização (Desconto no grafo residual):**
  * $(1 \rightarrow 2)$: $3 - 2 = 1$ (resta $1$ de folga) | Reversa $(2 \rightarrow 1)$ passa a ter $2$
  * $(2 \rightarrow 4)$: $2 - 2 = 0$ (**conexão saturada / bloqueada**) | Reversa $(4 \rightarrow 2)$ passa a ter $2$
* **Fluxo total acumulado:** $F = 0 + 2 = 2$

---

### Iteração 2 — Segundo Caminho Aumentante

* **Caminho encontrado pela BFS:** $1 \rightarrow 3 \rightarrow 4$
* **Capacidades disponíveis no caminho:**
  * Aresta $(1 \rightarrow 3)$: capacidade $4$
  * Aresta $(3 \rightarrow 4)$: capacidade $5$
* **Identificação do gargalo:** $\min(4, 5) = 4$
* **Atualização (Desconto no grafo residual):**
  * $(1 \rightarrow 3)$: $4 - 4 = 0$ (**conexão saturada / bloqueada**) | Reversa $(3 \rightarrow 1)$ passa a ter $4$
  * $(3 \rightarrow 4)$: $5 - 4 = 1$ (resta $1$ de folga) | Reversa $(4 \rightarrow 3)$ passa a ter $4$
* **Fluxo total acumulado:** $F = 2 + 4 = 6$

---

### Iteração 3 — Verificação de Encerramento

* A BFS parte novamente do vértice $1$ tentando alcançar o vértice $4$:
  * Pela aresta $(1 \rightarrow 2)$, ainda resta capacidade $1$, mas ao chegar no vértice $2$, a saída $(2 \rightarrow 4)$ possui capacidade residual $0$ (bloqueada).
  * Pela aresta $(1 \rightarrow 3)$, a capacidade residual já é $0$ (bloqueada).
  * A aresta $(4 \rightarrow 1)$ de capacidade $3$ aponta do destino para a origem, não formando caminho até $4$.
* Como **não existem mais caminhos aumentantes** da fonte ($1$) até o sumidouro ($4$), o algoritmo encerra.

---

## Comparação com a Saída Esperada

* **Fluxo Máximo Calculado:** $6$
* **Saída Esperada pelo Problema:** `6`
* **Status:** **Validado com sucesso.** A soma dos gargalos das rotas simultâneas respeita todas as capacidades das conexões e reproduz exatamente a saída esperada.

#  — Complexidade de Pior Caso (Tempo e Espaço)

Considerando $n$ computadores (vértices $V$) e $m$ conexões (arestas $E$), analisamos o comportamento assintótico de pior caso da solução utilizando **Edmonds-Karp** (Ford-Fulkerson com Busca em Largura).

---

## 1. Complexidade de Tempo

### Pior Caso: $O(n \cdot m^2)$ (ou $O(V \cdot E^2)$)

#### Justificativa Detalhada

1. **Custo de cada busca (BFS):** Em cada iteração, o algoritmo executa uma Busca em Largura para encontrar um caminho livre da origem até o destino. No pior caso, a BFS visita todos os vértices e percorre todas as arestas do grafo residual, levando tempo $O(n + m)$. Como em redes conectadas $m \ge n - 1$, esse custo simplifica-se para $O(m)$.
2. **Número máximo de iterações (caminhos aumentantes):** Como a BFS sempre seleciona o caminho aumentante com o **menor número de arestas**, demonstra-se que a distância (em saltos) da origem até qualquer vértice nunca diminui entre iterações. Por conta disso, cada uma das $m$ conexões pode atuar como gargalo crítico (aresta saturada) no máximo $\frac{n}{2}$ vezes, limitando o total de buscas a $O(n \cdot m)$.
3. **Multiplicação final:** Multiplicando o número máximo de iterações $O(n \cdot m)$ pelo custo de cada BFS $O(m)$, obtemos a complexidade de pior caso **$O(n \cdot m^2)$**.

#### Por que essa escolha é crítica para as restrições do problema?

* O método de **Ford-Fulkerson genérico** (usando DFS) possui complexidade $O(m \cdot F)$, onde $F$ é o valor do fluxo máximo. Como as capacidades podem chegar a $c \le 10^9$, $F$ poderia atingir a ordem de $10^{12}$, causando estouro de tempo (*Time Limit Exceeded*).
* O **Edmonds-Karp** elimina a dependência do valor das capacidades $c$, dependendo apenas da estrutura do grafo ($n \le 500$ e $m \le 1000$), o que garante execução rápida dentro do limite de tempo.

---

## 2. Complexidade de Espaço

### Pior Caso: $O(n + m)$ (Lista de Adjacência) ou $O(n^2)$ (Matriz de Adjacência)

#### Justificativa Detalhada

* **Com Lista de Adjacência — $O(n + m)$ (ou $O(V + E)$):**
  * Armazenamos uma lista para cada um dos $n$ computadores contendo as $m$ arestas originais e as $m$ arestas reversas do grafo residual, totalizando $2m$ registros de arestas.
  * Além do grafo, mantemos vetores auxiliares de tamanho $n$ para a fila da BFS, controle de vértices visitados e armazenamento dos pais de cada vértice no caminho encontrado.
  * Somando as estruturas, o consumo de memória é estritamente linear em relação ao tamanho da entrada: **$O(n + m)$**.

* **Alternativa com Matriz de Adjacência — $O(n^2)$ (ou $O(V^2)$):**
  * Caso se opte por armazenar as capacidades residuais em uma matriz bidimensional $n \times n$, o espaço utilizado será **$O(n^2)$** (para $n \le 500$, representa $250.000$ posições de memória, também totalmente viável).