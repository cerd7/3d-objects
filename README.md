# Cubo 3D em Java Swing

Este projeto é uma demonstração simples de renderização 3D feita em Java puro, usando Swing e `Graphics2D`.

A aplicação abre uma janela e desenha um cubo em rotação contínua. O efeito 3D não vem de uma engine externa: ele é calculado manualmente com vértices, rotação em eixos, projeção em perspectiva e desenho 2D na tela.

## O que o projeto faz

O arquivo `App.java` cria uma janela Swing de 800x600 pixels e adiciona o painel `Cubo3DonFill`, que desenha um cubo com faces coloridas.

O projeto também tem uma segunda implementação, `Cubo3DinLine`, que desenha o cubo apenas com linhas. Ela não é usada no ponto de entrada atual, mas serve como uma versão mais simples para entender a estrutura do cubo antes de trabalhar com faces preenchidas.

## Estrutura

```text
src/
  App.java              Ponto de entrada da aplicação
  Cubo3DonFill.java     Cubo com faces preenchidas e coloridas
  Cubo3DinLine.java     Cubo em wireframe, desenhado só com arestas
```

## Como executar

Você precisa ter um JDK instalado. O projeto não usa Maven, Gradle ou bibliotecas externas.

No PowerShell, a partir da raiz do projeto:

```powershell
javac -encoding UTF-8 -d bin src\App.java src\Cubo3DonFill.java src\Cubo3DinLine.java
java -cp bin App
```

Isso compila os arquivos Java para a pasta `bin` e depois executa a classe `App`.

## Como funciona

O cubo é definido por uma lista de vértices em três dimensões. Cada vértice possui coordenadas `x`, `y` e `z`.

Durante cada redesenho da tela, o painel:

1. Lê os vértices originais do cubo.
2. Aplica rotação usando seno e cosseno.
3. Projeta os pontos 3D para coordenadas 2D com uma perspectiva simples.
4. Desenha as faces ou arestas no `Graphics2D`.
5. Incrementa o ângulo de rotação.
6. Chama `repaint()` para continuar a animação.

Na prática, é uma mini pipeline gráfica feita na mão: modelo 3D, transformação, projeção e rasterização básica.

## Arquivos principais

### `App.java`

Cria a janela principal da aplicação e adiciona o painel responsável pelo desenho.

Atualmente ele usa:

```java
Cubo3DonFill painel = new Cubo3DonFill();
```

Se quiser testar a versão em linhas, basta trocar por:

```java
Cubo3DinLine painel = new Cubo3DinLine();
```

### `Cubo3DonFill.java`

Desenha o cubo com faces preenchidas. Ele define:

- oito vértices;
- seis faces;
- uma cor para cada face;
- rotação nos eixos X e Y;
- projeção em perspectiva;
- preenchimento dos polígonos na tela.

Essa é a versão visualmente mais completa do projeto.

### `Cubo3DinLine.java`

Desenha o mesmo cubo usando apenas suas doze arestas. É uma boa base para estudar a geometria antes de lidar com ordenação de faces, preenchimento e problemas de profundidade.

## Limitações atuais

O projeto é propositalmente simples. Ele não tem:

- controle de profundidade real;
- ordenação correta das faces por distância;
- iluminação;
- interação por teclado ou mouse;
- separação entre motor matemático e camada de desenho.

Isso significa que o cubo funciona como experimento visual e didático, mas ainda não é um renderizador 3D robusto.

## Próximos passos possíveis

Algumas melhorias naturais para evoluir o projeto:

- ordenar as faces pela profundidade antes de desenhar;
- adicionar controle de rotação com teclado ou mouse;
- separar as contas de matriz em uma classe própria;
- criar uma classe `Vector3D`;
- adicionar iluminação simples por produto escalar;
- trocar `repaint()` direto por um `javax.swing.Timer`;
- criar mais sólidos geométricos além do cubo.

O caminho mais interessante é transformar o código atual em um pequeno laboratório de gráficos 3D, mantendo a matemática visível em vez de esconder tudo dentro de uma biblioteca.
