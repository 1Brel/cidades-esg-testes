# language: pt

# Pilar ESG: Governança.
# Garante que somente usuários identificados acessem e alterem os dados
# ambientais da plataforma, base para a rastreabilidade das informações.

@regressivo @autenticacao
Funcionalidade: Cadastro e autenticação de usuários
  Como gestor da plataforma Cidades ESGInteligentes
  Quero que os usuários se cadastrem e se autentiquem com segurança
  Para que apenas pessoas identificadas registrem dados de emissões de carbono

  @contrato
  Cenário: Cadastro de usuário realizado com sucesso
    Dado que eu tenha os dados de um novo usuário com perfil "ADMIN"
    Quando eu enviar a requisição de cadastro de usuário
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter os dados do usuário cadastrado
    E a resposta deve estar em conformidade com o contrato "usuario"

  @contrato
  Cenário: Cadastro de usuário com e-mail já existente
    Dado que eu tenha os dados de um novo usuário com perfil "USER"
    E que esse usuário já esteja cadastrado na API
    Quando eu enviar a requisição de cadastro de usuário
    Então o status code da resposta deve ser 409
    E o campo "erro" do corpo da resposta deve ser "Usuário já cadastrado!"
    E a resposta deve estar em conformidade com o contrato "erro-conflito"

  Cenário: Cadastro de usuário com e-mail em formato inválido
    Dado que eu tenha os dados de um novo usuário com perfil "USER"
    E que o e-mail desse usuário seja "email-sem-formato-valido"
    Quando eu enviar a requisição de cadastro de usuário
    Então o status code da resposta deve ser 400
    E o campo "email" do corpo da resposta deve ser "O e-mail do usuário não é válido!"

  @contrato
  Cenário: Login realizado com sucesso
    Dado que exista um usuário cadastrado com perfil "ADMIN"
    Quando eu realizar o login com as credenciais desse usuário
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter um token de acesso
    E a resposta deve estar em conformidade com o contrato "token"

  Cenário: Login com e-mail em formato inválido é recusado
    Quando eu realizar o login com o e-mail "email-sem-formato-valido"
    Então o status code da resposta deve ser 400
    E o campo "email" do corpo da resposta deve ser "O e-mail do usuário não é válido!"

  # Achado dos testes: a API responde 403 (e não 401) para credenciais inválidas,
  # porque o Spring Security bloqueia a página de erro padrão (/error).
  @achado
  Cenário: Login com senha incorreta é recusado
    Dado que exista um usuário cadastrado com perfil "ADMIN"
    Quando eu realizar o login com a senha "senhaIncorreta1"
    Então o status code da resposta deve ser 403
