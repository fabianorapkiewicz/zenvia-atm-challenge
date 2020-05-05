# Desafio do Caixa Eletrônico

Resolução de exercício do processo seletivo para desenvolvedores de doftware da empresa Zenvia.

## PROBLEMA PROPOSTO
Desenvolva um programa que simule a entrega de notas quando um cliente efetuar um saque em um caixa eletrônico. Os requisitos básicos são os seguintes:

 - Entregar o menor número de notas;
 - Ser possível sacar o valor solicitado com as notas disponíveis;
 - Saldo do cliente infinito;
 - Quantidade de notas infinito (pode-se colocar um valor finito de cédulas para aumentar a dificuldade do problema);
 - Notas disponíveis de R$ 100,00; R$ 50,00; R$ 20,00 e R$ 10,00

**Alguns exemplos de comportamento esperado**:
 - Valor do Saque: R$ 30,00 – Resultado Esperado: Entregar 1 nota de R$20,00 e 1 nota de R$ 10,00.
 - Valor do Saque: R$ 80,00 – Resultado Esperado: Entregar 1 nota de R$50,00 1 nota de R$ 20,00 e 1 nota de R$ 10,00.


## SOLUÇÃO ADOTADA
Para simulação da operação de saque do caixa eletrônico foi desenvolvido uma Aplicação Web. Com isso, além da simplicidade para testar o simulador via uso de URLs, também foi possível demonstrar o conhecimento na construção e teste de aplicações Web com Spring em Java.

O caixa eletrônico está sendo representado por um componente chamado CashMachine que é composto por um validador e um conjunto de manipuladores que separam as notas, CashMachineValidator e CashMachineHandler respectivamente. 

O CashMachineValidator é o responsável por validar todo valor de saque (CashAmount) requisitado ao CashMachine. Dado a natureza do problema proposto pelo exercício, foi avaliado e aplicado o design pattern Chain of Responsability para resolver a dinâmica de contagem de notas que ocorre entre os manipuladores da máquina (CashMachineHandler). Em suma, há um manipulador para cada nota indicada nos requisitos do exercício: notas de 100, notas de 50, notas de 20 e notas de 10. 

Sendo assim, quando uma requisição de saque é feita para a máquina, ela é validada e então passada para o primeiro manipulador da cadeia de manipuladores (Chain) que separa as notas do valor que lhe compete, se puder. Caso não possa, passa a requisição para o próximo manipulador que terá o mesmo procedimento. As notas separadas pelos manipuladores serão depositadas cada uma no coletor da máquina, representado pelo componente CashCollector.

Após a máquina (CashMachine) processar uma requisição de saque válida, ela retorna as cédulas separadas em maços de dinheiro (WadOfCash), agrupados pelo valor da nota e quantidade. 

Visando aumentar a dificuldade da solução, foi permitido estabelecer limites de quantidade de notas para cada valor. Essa parametrização pode ser realizada no aquivo de propriedades do projeto informado adiante.
 
## Tecnologias utilizadas
- Java 11
- Spring Boot
- Junit 5
- Gradle
- Docker

## Pré requisitos para rodar aplicação
- Docker instalado na máquina onde vai rodar aplicação. (https://www.docker.com/)
- Nenhuma aplicação estar rodando na porta 8080 ou realizar a seguinte alteraração no arquivo deploy.sh em /zenvia-atm-challenge:  

> mudar de:

```
docker run -p 8080:8080 -d zenvia/atm 
```
> para:

```
docker run -p {porta-desejada}:8080 -d zenvia/atm 
```

## Para rodar aplicação
Para rodar a aplicação basta acessar o diretório /zenvia-atm-challenge e executar o seguinte comando no terminal:

```
bash deploy.sh
```

Este script irá:
1. Construir as imagens docker para a aplicação
2. Rodar um container docker para a imagem criada

Após rodar o script basta acessar via Browser o endereço: localhost:8080/terminal/saque/{quantia}, onde quantia é o valor a ser sacado.  

## Para limitar o número de notas
Para limitar o número de cada nota disponível basta acessar o diretório zenvia-atm-challenge/src/main/resources/ e alterar as seguintes propriedades do arquivo application.properties:

```
banknotes-limit.hundred = 
banknotes-limit.fifty = 
banknotes-limit.twenty = 
banknotes-limit.ten = 
```

*Será considerado como valor infinito quando não houver limite definido.
