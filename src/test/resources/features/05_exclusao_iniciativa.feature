# language: pt

# Pilar ESG: Governança (integridade e rastreabilidade).
# Garante que a remoção de dados ambientais seja controlada e efetiva.

@regressivo @iniciativas
Funcionalidade: Exclusão de iniciativas de redução de carbono
  Como gestor ambiental da cidade
  Quero excluir iniciativas cadastradas indevidamente
  Para que os relatórios de emissões reflitam apenas dados válidos

  Contexto:
    Dado que eu esteja autenticado como usuário com perfil "ADMIN"
    E que exista uma iniciativa cadastrada

  Cenário: Exclusão de uma iniciativa realizada com sucesso
    Quando eu excluir a iniciativa cadastrada
    Então o status code da resposta deve ser 204
    E a iniciativa excluída não deve mais ser encontrada

  # Achado dos testes: a API responde 403 (e não 404) ao excluir uma iniciativa
  # inexistente, porque a exceção não é tratada e o Spring Security bloqueia a
  # página de erro padrão (/error).
  @achado
  Cenário: Exclusão de uma iniciativa que já foi excluída
    Dado que exista uma iniciativa que já foi excluída
    Quando eu excluir novamente a iniciativa excluída
    Então o status code da resposta deve ser 403

  # Governança: só um administrador autenticado pode remover dados ambientais.
  Esquema do Cenário: Exclusão de iniciativa recusada com a credencial "<credencial>"
    Dado que eu acesse a API com a credencial "<credencial>"
    Quando eu excluir a iniciativa cadastrada
    Então o status code da resposta deve ser 403

    Exemplos:
      | credencial     |
      | sem token      |
      | token inválido |
      | perfil USER    |
