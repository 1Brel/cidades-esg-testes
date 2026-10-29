# language: pt

# Garante que as iniciativas de redução de carbono só sejam registrada com dados completos e válidos, e somente por usuários autorizados.

@regressivo @iniciativas
Funcionalidade: Cadastro de iniciativas de redução de carbono
  Como gestor ambiental da cidade
  Quero registrar iniciativas de redução de emissões de carbono
  Para acompanhar as metas de sustentabilidade com dados confiáveis

  @contrato
  Cenário: Cadastro de iniciativa realizado com sucesso
    Dado que eu esteja autenticado como usuário com perfil "ADMIN"
    E que eu tenha os seguintes dados da iniciativa:
      | campo            | valor                                      |
      | descricao        | Substituição da frota por ônibus elétricos |
      | categoria        | Transporte Sustentável                     |
      | status           | PLANEJADA                                  |
      | emissaoBaseTco2E | 5200.00                                    |
      | metaReducaoTco2E | 1500.00                                    |
    Quando eu enviar a requisição de cadastro de iniciativa
    Então o status code da resposta deve ser 201
    E o corpo da resposta deve conter os dados da iniciativa enviada
    E a resposta deve estar em conformidade com o contrato "iniciativa"

  # Compliance: nenhum dado ambiental incompleto ou incoerente pode ser registrado.
  Esquema do Cenário: Cadastro de iniciativa recusado por dado inválido em "<campo>"
    Dado que eu esteja autenticado como usuário com perfil "ADMIN"
    E que eu tenha os dados de uma iniciativa válida
    Mas o campo "<campo>" da iniciativa tenha o valor "<valor>"
    Quando eu enviar a requisição de cadastro de iniciativa
    Então o status code da resposta deve ser 400
    E o campo "<campo>" do corpo da resposta deve ser "<mensagem>"

    Exemplos:
      | campo            | valor | mensagem                           |
      | descricao        |       | A descrição é obrigatória          |
      | categoria        |       | A categoria é obrigatória          |
      | status           |       | O status é obrigatório             |
      | emissaoBaseTco2E | -10   | must be greater than or equal to 0 |
      | metaReducaoTco2E | -5    | must be greater than or equal to 0 |

  # Só um administrador autenticado pode registrar dados ambientais.
  Esquema do Cenário: Cadastro de iniciativa recusado com a credencial "<credencial>"
    Dado que eu acesse a API com a credencial "<credencial>"
    E que eu tenha os dados de uma iniciativa válida
    Quando eu enviar a requisição de cadastro de iniciativa
    Então o status code da resposta deve ser 403

    Exemplos:
      | credencial     |
      | sem token      |
      | token inválido |
      | perfil USER    |
