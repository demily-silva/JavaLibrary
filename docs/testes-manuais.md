# Testes manuais do sistema JavaLibrary

Este arquivo registra alguns testes feitos manualmente pela interface gráfica do sistema.

## Teste 1: Cadastrar livro

Dados usados:

- Título: Dom Casmurro
- Autor: Machado de Assis
- ISBN: 123
- Cópias disponíveis: 1

Resultado esperado:

- O livro deve aparecer na tabela de livros cadastrados.

Resultado obtido:

- O livro foi cadastrado e apareceu corretamente na tabela.

Status: passou.

## Teste 2: Cadastrar usuário

Dados usados:

- ID: 1
- Nome: Maria Silva
- Contato: maria@email.com

Resultado esperado:

- O usuário deve aparecer na tabela de usuários cadastrados.

Resultado obtido:

- O usuário foi cadastrado e apareceu corretamente na tabela.

Status: passou.

## Teste 3: Fazer empréstimo

Dados usados:

- ISBN do livro: 123
- ID do usuário: 1

Resultado esperado:

- O empréstimo deve aparecer na tabela de empréstimos ativos.
- A quantidade de cópias disponíveis do livro deve diminuir de 1 para 0.

Resultado obtido:

- O empréstimo apareceu na tabela de empréstimos ativos.
- A quantidade de cópias disponíveis foi atualizada corretamente.

Status: passou.

## Teste 4: Impedir empréstimo sem cópias disponíveis

Dados usados:

- ISBN do livro: 123
- ID do usuário: 1

Resultado esperado:

- O sistema deve impedir o empréstimo, pois o livro está sem cópias disponíveis.

Resultado obtido:

- O sistema mostrou uma mensagem de erro e não fez um novo empréstimo.

Status: passou.

## Teste 5: Devolver livro

Dados usados:

- Empréstimo ativo do livro Dom Casmurro para Maria Silva.

Resultado esperado:

- O empréstimo deve sair da tabela de empréstimos ativos.
- A quantidade de cópias disponíveis do livro deve voltar de 0 para 1.

Resultado obtido:

- O empréstimo saiu da lista de ativos.
- A quantidade de cópias disponíveis foi atualizada corretamente.

Status: passou.

## Teste 6: Impedir remoção de livro emprestado

Resultado esperado:

- O sistema não deve permitir remover um livro que possui empréstimo ativo.

Resultado obtido:

- O sistema mostrou uma mensagem de erro e o livro não foi removido.

Status: passou.

## Teste 7: Impedir remoção de usuário com empréstimo ativo

Resultado esperado:

- O sistema não deve permitir remover um usuário que possui empréstimo ativo.

Resultado obtido:

- O sistema mostrou uma mensagem de erro e o usuário não foi removido.

Status: passou.

## Teste 8: Impedir ISBN duplicado

Resultado esperado:

- O sistema não deve permitir cadastrar dois livros com o mesmo ISBN.

Resultado obtido:

- O sistema mostrou uma mensagem de erro e o segundo livro não foi cadastrado.

Status: passou.

## Teste 9: Impedir ID de usuário duplicado

Resultado esperado:

- O sistema não deve permitir cadastrar dois usuários com o mesmo ID.

Resultado obtido:

- O sistema mostrou uma mensagem de erro e o segundo usuário não foi cadastrado.

Status: passou.

## Teste 10: Validar campos vazios e número de cópias

Resultado esperado:

- O sistema deve mostrar erro quando campos obrigatórios ficarem vazios.
- O sistema deve mostrar erro quando a quantidade de cópias não for um número inteiro.

Resultado obtido:

- O sistema mostrou mensagens de erro para campos vazios e para quantidade inválida.

Status: passou.
