package br.edu.fafic.barberproject.main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 */
public class Connection1 {


    public static void main(String[] args) throws IOException, TimeoutException {
        Agendamento ag = new Agendamento("João Alves", "26/10/2021");
        ag.excute(ag);
    }
}




