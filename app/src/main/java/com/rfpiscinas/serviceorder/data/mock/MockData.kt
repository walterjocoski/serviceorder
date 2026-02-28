package com.rfpiscinas.serviceorder.data.mock

import com.rfpiscinas.serviceorder.data.model.*

object MockData {
    
    // Funcionários
    val employees = listOf(
        User(
            id = 1,
            name = "João Silva",
            email = "joao@rfpiscinas.com",
            phone = "(11) 98765-1111",
            address = "Rua das Acácias, 100 - Vila Mariana, São Paulo - SP",
            role = UserRole.EMPLOYEE,
            active = true,
            startDate = "2023-01-15"
        ),
        User(
            id = 2,
            name = "Maria Santos",
            email = "maria@rfpiscinas.com",
            phone = "(11) 98765-2222",
            address = "Av. Rebouças, 200 - Pinheiros, São Paulo - SP",
            role = UserRole.EMPLOYEE,
            active = true,
            startDate = "2023-03-20"
        )
    )
    
    // Gerente
    val manager = User(
        id = 3,
        name = "Carlos Gerente",
        email = "gerente@rfpiscinas.com",
        phone = "(11) 98765-3333",
        address = "Rua Augusta, 300 - Consolação, São Paulo - SP",
        role = UserRole.MANAGER,
        active = true,
        startDate = "2022-01-10"
    )
    
    // Clientes
    val clients = listOf(
        Client(
            id = 1,
            name = "Condomínio Jardim das Flores",
            cpfCnpj = "12.345.678/0001-90",
            address = "Rua das Flores, 123 - Jardim Paulista, São Paulo - SP",
            phone = "(11) 98765-4321",
            email = "contato@condominioflores.com.br",
            active = true
        ),
        Client(
            id = 2,
            name = "Residência Silva",
            cpfCnpj = "123.456.789-00",
            address = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP",
            phone = "(11) 91234-5678",
            email = "silva@email.com",
            active = true
        ),
        Client(
            id = 3,
            name = "Condomínio Parque das Águas",
            cpfCnpj = "98.765.432/0001-10",
            address = "Rua das Águas, 456 - Morumbi, São Paulo - SP",
            phone = "(11) 99876-5432",
            email = "contato@parqueaguas.com.br",
            active = true
        ),
        Client(
            id = 4,
            name = "Residência Costa",
            cpfCnpj = "987.654.321-00",
            address = "Rua dos Pinheiros, 789 - Pinheiros, São Paulo - SP",
            phone = "(11) 93456-7890",
            email = "costa@email.com",
            active = false // Cliente inativo para teste
        )
    )
    
    // Serviços
    val services = listOf(
        Service(
            id = 1,
            name = "Limpeza de Borda",
            type = "Limpeza",
            usesProducts = true
        ),
        Service(
            id = 2,
            name = "Aplicação de Cloro",
            type = "Tratamento Químico",
            usesProducts = true
        ),
        Service(
            id = 3,
            name = "Aspiração do Fundo",
            type = "Limpeza",
            usesProducts = false
        ),
        Service(
            id = 4,
            name = "Limpeza de Filtro",
            type = "Manutenção",
            usesProducts = false
        ),
        Service(
            id = 5,
            name = "Aplicação de Algicida",
            type = "Tratamento Químico",
            usesProducts = true
        ),
        Service(
            id = 6,
            name = "Teste de pH",
            type = "Análise",
            usesProducts = false
        ),
        Service(
            id = 7,
            name = "Escovação de Paredes",
            type = "Limpeza",
            usesProducts = false
        )
    )
    
    // Produtos
    val products = listOf(
        Product(
            id = 1,
            name = "Cloro Granulado",
            unitMeasure = UnitMeasure.GRAMS
        ),
        Product(
            id = 2,
            name = "Algicida",
            unitMeasure = UnitMeasure.ML
        ),
        Product(
            id = 3,
            name = "Barrilha",
            unitMeasure = UnitMeasure.KG
        ),
        Product(
            id = 4,
            name = "Clarificante",
            unitMeasure = UnitMeasure.ML
        ),
        Product(
            id = 5,
            name = "Redutor de pH",
            unitMeasure = UnitMeasure.GRAMS
        ),
        Product(
            id = 6,
            name = "Elevador de pH",
            unitMeasure = UnitMeasure.GRAMS
        )
    )
    
    // Ordens de Serviço - Com múltiplos prestadores
    val completedOrders = listOf(
        // OS do João
        ServiceOrder(
            id = 1,
            clientId = 1,
            clientName = "Condomínio Jardim das Flores",
            clientAddress = "Rua das Flores, 123 - Jardim Paulista, São Paulo - SP",
            employeeId = 1,
            employeeName = "João",
            status = OrderStatus.COMPLETED,
            startDateTime = "2024-02-10 08:00",
            endDateTime = "2024-02-10 10:00",
            items = listOf(
                ServiceOrderItem(
                    id = 1,
                    service = services[0],
                    observations = "Borda com muita sujeira acumulada",
                    products = listOf(
                        ServiceOrderProduct(1, products[0], 500.0)
                    )
                ),
                ServiceOrderItem(
                    id = 2,
                    service = services[1],
                    observations = "",
                    products = listOf(
                        ServiceOrderProduct(2, products[0], 300.0)
                    )
                ),
                ServiceOrderItem(
                    id = 3,
                    service = services[2],
                    observations = ""
                )
            )
        ),
        ServiceOrder(
            id = 2,
            clientId = 2,
            clientName = "Residência Silva",
            clientAddress = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP",
            employeeId = 1,
            employeeName = "João",
            status = OrderStatus.COMPLETED,
            startDateTime = "2024-02-12 09:00",
            endDateTime = "2024-02-12 10:00",
            items = listOf(
                ServiceOrderItem(
                    id = 4,
                    service = services[1],
                    observations = "",
                    products = listOf(
                        ServiceOrderProduct(3, products[0], 200.0)
                    )
                ),
                ServiceOrderItem(
                    id = 5,
                    service = services[4],
                    observations = "Água com aspecto esverdeado",
                    products = listOf(
                        ServiceOrderProduct(4, products[1], 150.0)
                    )
                )
            )
        ),
        ServiceOrder(
            id = 3,
            clientId = 3,
            clientName = "Condomínio Parque das Águas",
            clientAddress = "Rua das Águas, 456 - Morumbi, São Paulo - SP",
            employeeId = 1,
            employeeName = "João",
            status = OrderStatus.COMPLETED,
            startDateTime = "2024-02-14 07:30",
            endDateTime = "2024-02-14 10:30",
            items = listOf(
                ServiceOrderItem(
                    id = 6,
                    service = services[0],
                    observations = ""
                ),
                ServiceOrderItem(
                    id = 7,
                    service = services[2],
                    observations = ""
                ),
                ServiceOrderItem(
                    id = 8,
                    service = services[3],
                    observations = "Filtro necessitava limpeza profunda"
                ),
                ServiceOrderItem(
                    id = 9,
                    service = services[5],
                    observations = "pH ajustado para 7.4"
                )
            )
        ),
        // OS da Maria
        ServiceOrder(
            id = 4,
            clientId = 1,
            clientName = "Condomínio Jardim das Flores",
            clientAddress = "Rua das Flores, 123 - Jardim Paulista, São Paulo - SP",
            employeeId = 2,
            employeeName = "Maria",
            status = OrderStatus.COMPLETED,
            startDateTime = "2024-02-11 08:30",
            endDateTime = "2024-02-11 11:00",
            items = listOf(
                ServiceOrderItem(
                    id = 10,
                    service = services[1],
                    observations = "Tratamento de choque realizado",
                    products = listOf(
                        ServiceOrderProduct(5, products[0], 800.0),
                        ServiceOrderProduct(6, products[1], 200.0)
                    )
                ),
                ServiceOrderItem(
                    id = 11,
                    service = services[5],
                    observations = "pH estava em 6.8, ajustado para 7.2"
                ),
                ServiceOrderItem(
                    id = 12,
                    service = services[6],
                    observations = ""
                )
            )
        ),
        ServiceOrder(
            id = 5,
            clientId = 3,
            clientName = "Condomínio Parque das Águas",
            clientAddress = "Rua das Águas, 456 - Morumbi, São Paulo - SP",
            employeeId = 2,
            employeeName = "Maria",
            status = OrderStatus.IN_PROGRESS,
            startDateTime = "2024-02-15 09:00",
            endDateTime = null,
            items = listOf(
                ServiceOrderItem(
                    id = 13,
                    service = services[0],
                    observations = "Borda apresentando excesso de algas"
                ),
                ServiceOrderItem(
                    id = 14,
                    service = services[4],
                    observations = "",
                    products = listOf(
                        ServiceOrderProduct(7, products[1], 250.0)
                    )
                )
            )
        ),
        ServiceOrder(
            id = 6,
            clientId = 2,
            clientName = "Residência Silva",
            clientAddress = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP",
            employeeId = 2,
            employeeName = "Maria",
            status = OrderStatus.CREATED,
            startDateTime = "2024-02-16 14:00",
            endDateTime = null,
            items = listOf(
                ServiceOrderItem(
                    id = 15,
                    service = services[3],
                    observations = ""
                ),
                ServiceOrderItem(
                    id = 16,
                    service = services[2],
                    observations = ""
                )
            )
        )
    )
    
    // Função auxiliar para obter apenas clientes ativos
    fun getActiveClients() = clients.filter { it.active }
    
    // Função auxiliar para obter apenas funcionários ativos
    fun getActiveEmployees() = employees.filter { it.active }
}
