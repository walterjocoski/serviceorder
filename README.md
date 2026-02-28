# RF Piscinas - Sistema de Ordem de Serviço

Aplicativo Android para gerenciamento de ordens de serviço de limpeza e manutenção de piscinas.

## 🚀 Tecnologias

- **Kotlin** - Linguagem de programação
- **Jetpack Compose** - UI moderna e declarativa
- **Material 3** - Design system
- **MVVM + Clean Architecture** - Arquitetura
- **Hilt** - Injeção de dependências
- **Room** - Banco de dados local (offline)
- **Retrofit** - Cliente HTTP
- **Kotlinx Serialization** - Serialização JSON
- **Navigation Compose** - Navegação entre telas

## 📱 Funcionalidades Implementadas

### Perfil Funcionário
- ✅ Login com autenticação
- ✅ Visualizar lista de OS finalizadas (ordenadas da mais recente)
- ✅ Criar nova ordem de serviço
- ✅ Selecionar cliente via combobox
- ✅ Adicionar serviços executados via combobox
- ✅ Adicionar produtos utilizados com quantidade
- ✅ Adicionar observações aos serviços
- ✅ Visualizar detalhes completos de uma OS

### Perfil Gerente
- ✅ Dashboard com acesso às funcionalidades
- 🚧 Cadastro de clientes (em desenvolvimento)
- 🚧 Cadastro de funcionários (em desenvolvimento)
- 🚧 Cadastro de serviços (em desenvolvimento)
- 🚧 Cadastro de produtos (em desenvolvimento)
- 🚧 Geração de relatórios (em desenvolvimento)

## 🎨 Design

- **Tema**: Cores azul piscina/água
- **Idioma**: Português do Brasil
- **UX**: Comboboxes para minimizar digitação
- **Responsivo**: Adaptado para diferentes tamanhos de tela

## 🔐 Credenciais de Teste

**Funcionário:**
- Email: `funcionario@rfpiscinas.com`
- Senha: qualquer

**Gerente:**
- Email: `gerente@rfpiscinas.com`
- Senha: qualquer

## 📦 Estrutura do Projeto

```
app/src/main/java/com/rfpiscinas/serviceorder/
├── data/
│   ├── model/          # Modelos de dados
│   └── mock/           # Dados mockados
├── ui/
│   ├── theme/          # Tema Material 3
│   ├── navigation/     # Navegação
│   └── screens/        # Telas
│       ├── login/
│       ├── employee/
│       └── manager/
├── MainActivity.kt
└── RFPiscinasApplication.kt
```

## 🏗️ Como Executar

1. Abra o projeto no Android Studio
2. Sincronize o Gradle
3. Execute no emulador ou dispositivo físico (API 26+)

## 📝 Dados Mockados

O aplicativo contém dados mockados realistas:
- 4 clientes (condomínios e residências)
- 2 funcionários
- 1 gerente
- 7 serviços de limpeza de piscina
- 6 produtos químicos
- 3 ordens de serviço finalizadas

## 🔄 Próximos Passos

1. Implementar telas de cadastro do gerente
2. Integrar com backend REST
3. Implementar sincronização offline
4. Adicionar geração de relatórios PDF
5. Implementar notificações push
6. Adicionar testes unitários e de UI

## 👨‍💻 Desenvolvido para

**RF Piscinas** - Empresa de limpeza e manutenção de piscinas

---

**Versão:** 1.0.0  
**Última atualização:** Fevereiro 2026
