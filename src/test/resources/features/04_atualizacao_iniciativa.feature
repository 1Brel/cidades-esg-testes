# language: pt

# Pilares ESG: Ambiental e Governança.
# Garante que a evolução das iniciativas (status e metas) seja registrada
# corretamente e apenas por usuários autorizados.

@regressivo @iniciativas
Funcionalidade: Atualização de iniciativas de redução de carbono
  Como gestor ambiental da cidade
  Quero atualizar o andamento e as metas das iniciativas
  Para manter os indicadores de sustentabilidade sempre corretos

  Contexto:
    Dado que eu esteja autenticado como usuário com perfil "ADMIN"
    E que exista uma iniciativa cadastrada

  @contrato
  Cenário: Atualização de uma iniciativa realizada com sucesso
    Quando eu atualizar a iniciativa cadastrada com os seguintes dados:
      | campo            | valor        |
      | status           | EM_ANDAMENTO |
      | metaReducaoTco2E | 2100.00      |
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve refletir os dados atualizados
    E a resposta deve estar em conformidade com o contrato "iniciativa"

  Cenário: Atualização de uma iniciativa que já foi excluída
    Dado que exista uma iniciativa que já foi excluída
    Quando eu tentar atualizar a iniciativa excluída
    Então o status code da resposta deve ser 404

  # Governança: só um administrador autenticado pode alterar dados ambientais.
  Esquema do Cenário: Atualização de iniciativa recusada com a credencial "<credencial>"
    Dado que eu acesse a API com a credencial "<credencial>"
    Quando eu atualizar a iniciativa cadastrada com os seguintes dados:
      | campo  | valor     |
      | status | CONCLUIDA |
    Então o status code da resposta deve ser 403

    Exemplos:
      | credencial     |
      | sem token      |
      | token inválido |
      | perfil USER    |
