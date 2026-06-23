# JavaLibrary

Sistema de gerenciamento de biblioteca desenvolvido em Java para a disciplina de Programação Orientada a Objetos.

## Funcionalidades

- Cadastro, edição, remoção e busca de livros.
- Cadastro, edição, remoção e busca de usuários.
- Empréstimo de livros para usuários cadastrados.
- Devolução de livros emprestados.
- Controle de cópias disponíveis.
- Bloqueio de remoção de livros ou usuários com empréstimos ativos.
- Salvamento e carregamento dos dados em arquivos de texto.
- Interface gráfica feita com Swing.

## Estrutura do projeto

```text
src/javalibrary/
├── exception/    Exceções próprias do sistema
├── model/        Classes principais do sistema
├── persistence/  Leitura e escrita dos arquivos
├── service/      Regras da biblioteca
├── view/         Interface gráfica
└── Main.java     Classe principal
```

## Como executar

O projeto foi feito em Java e não usa bibliotecas externas.

Antes de executar, é necessário ter o JDK instalado.

### Executando no Windows

No PowerShell, dentro da pasta do projeto, use:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out javalibrary.Main
```

### Executando no Linux

No terminal, dentro da pasta do projeto, use:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out javalibrary.Main
```

## Arquivos de dados

Os dados são salvos automaticamente na pasta `data`, em arquivos de texto:

```text
data/books.txt
data/patrons.txt
data/loans.txt
```

Esses arquivos são criados durante o uso do sistema.

## Observação

O sistema usa conceitos de Programação Orientada a Objetos, como classes, objetos, encapsulamento, associação entre classes, herança em exceções e tratamento de erros.
