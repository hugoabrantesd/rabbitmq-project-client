package br.edu.fafic.barberproject.main;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Agendamento {

    private final String id;
    private String nameClient;
    private String dateScheduling;

    public Agendamento(String nameCliente, String dateScheduling) {
        this.nameClient = nameCliente;
        this.dateScheduling = dateScheduling;
        //this.id = ++Constants.ID;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getDateScheduling() {
        return dateScheduling;
    }

    public void setDateScheduling(String dateScheduling) {
        this.dateScheduling = dateScheduling;
    }

    public void excute(Agendamento ag) throws IOException, TimeoutException {
        final Scanner scanner = new Scanner(System.in);

        int op;
        while (true) {
            System.out.print("\n======== APP BARBEARIA --> CLIENTES ========\n" +
                    "1- Enviar solicitação de agendamento\n" +
                    "0- Sair" +
                    "\n==>> "
            );
            op = scanner.nextInt();
            if (op == 1) {
                enviarSolicitacao(ag);
                //receberResposta();
            } else {
                System.exit(0);
                break;
            }
        }

    }


    public void enviarSolicitacao(Agendamento agendamento) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (com.rabbitmq.client.Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            //1º queue name
            channel.exchangeDeclare(Constants.EXCHANGE_NAME, "direct");
            channel.queueDeclare(this.getNameClient(), false, false, false, null);

            //Agendamento agendamento = new Agendamento("Hugo", date);
            String message = new Gson().toJson(agendamento);

            //1º exchange, 2º queue name
            channel.basicPublish(Constants.EXCHANGE_NAME, Constants.BARBER_ROUTING_KEY,
                    null, message.getBytes());

            System.out.println("Mensagem enviada!!!");
            System.out.println("--> Aguardando resposta".toUpperCase());
            receberResposta();

        }
    }

    public void receberResposta() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(Constants.EXCHANGE_RESPONSE, "direct");
        channel.queueDeclare(this.getNameClient(), false, false, false, null);
        channel.queueBind(this.getNameClient(), Constants.EXCHANGE_RESPONSE, Constants.RESPONSE_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String str = new String(delivery.getBody());
            System.out.println("\n" + "*** Você tem uma mensagem -> " +
                    str + "\n======================================================================================");
            System.out.print("\n======== APP BARBEARIA ========\n" +
                    "1- Enviar solicitação de agendamento\n" +
                    "0- Sair" +
                    "\n==>> "
            );
        };

        channel.basicConsume(this.getNameClient(), true, deliverCallback, consumerTag -> {
        });


//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection2 connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        System.out.println(this.getNameClient().toUpperCase() + " aguardando resposta...");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String str = new String(delivery.getBody());111
//            System.out.println("\n" + this.getNameClient() + " recebeu uma mensagem: " +
//                    str + "\n==============================");
//            //System.out.println(str);
//        };
//        channel.basicConsume(this.getNameClient(), true, deliverCallback, consumerTag -> {
//
//        });
    }
}
