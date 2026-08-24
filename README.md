

Diagrama de classe UML do projeto:
![class-diagram-2.png](docs/class-diagram/class-diagram-2.png)

## CI

O projeto utiliza GitHub Actions para realizar a Integração Contínua (CI) automaticamente em Pull Requests direcionados à branch main. A pipeline é executada antes do merge e garante que as alterações estejam formatadas, compilando corretamente e com os testes passando.

### Fluxo
```
 feature/login
       ↓
Pull Request → main
       ↓
 GitHub Actions
       ↓
┌───────────────┐
│ ✓ Formatting  │
│ ✓ Build       │
│ ✓ Tests       │
└───────────────┘
       ↓
   Code Review
       ↓
     Merge
```

### Verificações realizadas
_Formatting_: o Spotless verifica se o código segue o padrão de formatação definido no projeto.
_Build_: o Maven executa clean verify, garantindo que o projeto compile e seja empacotado corretamente.
_Tests_: os testes automatizados são executados durante o processo de verify.

### Execução local
As mesmas verificações podem ser executadas localmente antes de abrir um Pull Request:
```shell
cd equipment-maintenance-control-backend
```

#### Verificar formatação
```shell
./mvnw spotless:check
```

#### Corrigir formatação automaticamente
```shell
./mvnw spotless:apply
```

#### Executar build e testes
```shell
./mvnw clean verify
```

## Pull Requests

A main é protegida e o CI deve ser aprovado antes que um Pull Request possa ser integrado. Dessa forma, alterações que apresentem problemas de formatação, compilação ou testes não podem ser incorporadas à branch principal.