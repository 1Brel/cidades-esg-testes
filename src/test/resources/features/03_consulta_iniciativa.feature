# language: pt

# Pilar ESG: Governança (transparência).
# Garante que as iniciativas registradas possam ser consultadas de forma
# íntegra por qualquer usuário autenticado, e nunca sem autenticação.

@regressivo @iniciativas
Funcionalidade: Consulta de iniciativas de redução de carbono
  Como gestor ambiental da cidade
  Quero consultar as iniciativas registradas
  Para acompanhar a evolução das metas de redução de emissões

  Contexto:
    Dado que eu esteja autenticado como usuário com perfil "ADMIN"
    E que exista uma iniciativa cadastrada

  @contrato
  Cenário: Listagem de todas as iniciativas
    Quando eu buscar todas as iniciativas
    Então o status code da resposta deve ser 200
    E a lista de iniciativas deve conter a iniciativa cadastrada
    E a resposta deve estar em conformidade com o contrato "lista-iniciativas"

  # Transparência: qualquer usuário autenticado pode consultar os dados ambientais.
  Cenário: Usuário com perfil comum pode consultar as iniciativas
    Dado que eu acesse a API com a credencial "perfil USER"
    Quando eu buscar todas as iniciativas
    Então o status code da resposta deve ser 200
    E a lista de iniciativas deve conter a iniciativa cadastrada

  @contrato
  Cenário: Busca de uma iniciativa pelo ID
    Quando eu buscar a iniciativa cadastrada pelo seu ID
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter os dados da iniciativa cadastrada
    E a resposta deve estar em conformidade com o contrato "iniciativa"

  Cenário: Busca de uma iniciativa que já foi excluída
    Dado que exista uma iniciativa que já foi excluída
    Quando eu buscar a iniciativa excluída pelo seu ID
    Então o status code da resposta deve ser 404

  Esquema do Cenário: Listagem de iniciativas recusada com a credencial "<credencial>"
    Dado que eu acesse a API com a credencial "<credencial>"
    Quando eu buscar todas as iniciativas
    Então o status code da resposta deve ser 403

    Exemplos:
      | credencial     |
      | sem token      |
      | token inválido |
