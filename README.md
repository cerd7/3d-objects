```text
   _____      _             ____  ____  
  / ____|    | |           |___ \|  _ \ 
 | |    _   _| |__   ___     __) | | | |
 | |   | | | | '_ \ / _ \   |__ <| | | |
 | |___| |_| | |_) | (_) |  ___) | |_| |
  \_____\__,_|_.__/ \___/  |____/|____/ 
```

# Cubo 3D em Java Swing

Renderizador 3D didático feito em Java puro. O projeto abre uma janela Swing de 800x600, cria uma malha de cubo, aplica rotação em 3D, projeta os vértices para 2D e desenha as faces com `Graphics2D`.

Não usa engine gráfica, OpenGL, JavaFX, Maven ou Gradle. O objetivo aqui é expor a matemática e a estrutura mínima de uma pipeline 3D.

## Stack utilizada

O projeto depende apenas da biblioteca padrão do Java:

- `javax.swing.JFrame`: janela principal.
- `javax.swing.JPanel`: superfície onde o cubo é desenhado.
- `javax.swing.Timer`: loop simples de animação, rodando a cada 16 ms.
- `java.awt.Graphics2D`: API de desenho 2D usada para preencher e contornar polígonos.
- `java.awt.Polygon`: representação 2D das faces do cubo depois da projeção.
- `java.awt.Color`: cores das faces.
- `java.util.List` e `ArrayList`: armazenamento das entidades da cena.
- `java.lang.Math`: seno e cosseno usados nas rotações.

Sem dependências externas. Isso facilita rodar o projeto, mas também significa que recursos como profundidade, iluminação e clipping precisam ser implementados manualmente.

## Estrutura do projeto

```text
src/
  cubo3d/
    app/
      App.java              Ponto de entrada. Monta janela, cena, câmera e renderizador.

    math/
      Vector2.java          Vetor 2D usado após a projeção.
      Vector3.java          Vetor 3D imutável com rotações em X, Y e Z.

    model/
      Cube.java             Cria a malha do cubo a partir de um tamanho.
      Mesh.java             Guarda vértices, faces, arestas e cores.

    scene/
      Camera.java           Projeta pontos 3D em coordenadas 2D.
      Entity.java           Junta uma Mesh com um Transform.
      Scene.java            Armazena entidades e atualiza a rotação.
      Transform.java        Aplica rotação aos vértices.

    ui/
      RenderPanel.java      Painel Swing, fundo preto, Timer e repaint.
      Renderer.java         Interface de renderização.
      SolidRenderer.java    Renderizador de faces preenchidas.

  Cubo3DonFill.java         Protótipo antigo comentado.
  Cubo3DinLine.java         Protótipo antigo comentado.
```

Fluxo principal:

```text
App
  -> Scene
    -> Entity
      -> Mesh + Transform
  -> Camera
  -> RenderPanel
    -> SolidRenderer
      -> Graphics2D
```

## Como baixar e rodar localmente

Requisito: JDK instalado. O projeto foi validado com `javac 17`.

Verifique se o Java está disponível:

```powershell
java -version
javac -version
```

Baixando com Git:

```powershell
git clone <url-do-repositorio>
cd cubo
```

Se você baixou como `.zip`, extraia o arquivo e abra o terminal na pasta raiz do projeto, onde ficam `README.md` e `src/`.

Compilando e rodando no PowerShell:

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Recurse -Filter *.java).FullName
java -cp bin cubo3d.app.App
```

Compilando e rodando no Linux/macOS:

```bash
mkdir -p bin
find src -name "*.java" -print | xargs javac -encoding UTF-8 -d bin
java -cp bin cubo3d.app.App
```

Para limpar os arquivos compilados no PowerShell:

```powershell
Remove-Item -Recurse -Force bin
```

No Linux/macOS:

```bash
rm -rf bin
```

## Pipeline de renderização

O cubo é criado em `Cube.create(200)`. O método calcula metade do tamanho (`size / 2.0`) e monta a geometria em torno da origem:

- 8 vértices, um para cada canto do cubo.
- 6 faces, cada uma com 4 índices de vértices.
- 12 arestas, já preparadas para um futuro renderizador wireframe.
- 6 cores, uma por face.

O `App` cria uma `Scene`, adiciona uma `Entity` com `Mesh` e `Transform`, define uma `Camera(400)` e usa `SolidRenderer` para desenhar as faces.

O loop de animação fica em `RenderPanel`:

```java
Timer timer = new Timer(16, e -> {
    scene.update(0.01);
    repaint();
});
```

Na prática, isso tenta redesenhar perto de 60 FPS. O `dt` usado hoje é fixo (`0.01`), então ele controla a velocidade angular de forma simples, não baseada no tempo real medido.

## Matemática usada

O projeto trabalha com um sistema cartesiano simples:

- `x`: eixo horizontal.
- `y`: eixo vertical.
- `z`: profundidade.

Cada vértice começa como `Vector3(x, y, z)`. Antes de desenhar, o `Transform` aplica rotações em sequência:

```java
return v.rotateX(rotX).rotateY(rotY).rotateZ(rotZ);
```

As rotações usam as fórmulas clássicas de matriz de rotação.

Rotação no eixo X:

```text
y' = y * cos(a) - z * sin(a)
z' = y * sin(a) + z * cos(a)
x' = x
```

Rotação no eixo Y:

```text
x' = x * cos(a) - z * sin(a)
z' = x * sin(a) + z * cos(a)
y' = y
```

Rotação no eixo Z:

```text
x' = x * cos(a) - y * sin(a)
y' = x * sin(a) + y * cos(a)
z' = z
```

Depois da rotação, a câmera faz uma projeção em perspectiva:

```text
escala = distancia / (distancia + z)
x2d = x * escala
y2d = y * escala
```

Com `Camera(400)`, objetos com `z` maior ficam proporcionalmente menores. É uma perspectiva simples, suficiente para o cubo girar com sensação de profundidade.

Depois disso, `SolidRenderer` desloca o ponto projetado para o centro da janela:

```text
telaX = centroX + x2d
telaY = centroY + y2d
```

Cada face vira um `Polygon`, é preenchida com a cor correspondente e recebe contorno preto.

## Limitações técnicas

O renderizador atual não ordena faces por profundidade. Ele desenha na ordem do array `mesh.faces`, então alguns ângulos podem mostrar uma face errada por cima de outra.

Também não há:

- `z-buffer`;
- clipping perto da câmera;
- iluminação;
- cálculo de normais;
- textura;
- movimento de câmera;
- translação e escala no `Transform`;
- controle por teclado ou mouse;
- renderizador wireframe ativo, apesar das arestas já existirem em `Mesh`;
- testes automatizados.

O ponto fraco mais importante é profundidade. Antes de melhorar visual, vale resolver ordenação de faces ou implementar um `z-buffer` simples.

## Próximas melhorias

Uma sequência prática:

1. Ordenar as faces pela profundidade média antes de chamar `fillPolygon`.
2. Criar `WireframeRenderer` usando `mesh.edges`.
3. Adicionar entrada por teclado ou mouse para controlar rotação.
4. Incluir translação e escala em `Transform`.
5. Migrar transformação para matriz 4x4 quando o acoplamento começar a crescer.
6. Calcular normal de face e iluminação difusa com produto escalar.
7. Criar outros sólidos: pirâmide, prisma, plano e malhas carregadas de arquivo.
8. Adicionar testes para `Vector3`, `Camera.project()` e `Cube.create()`.

O projeto está num bom ponto para virar um mini laboratório de computação gráfica. Pequeno o bastante para entender, mas já separado em camadas para crescer sem virar bagunça.
