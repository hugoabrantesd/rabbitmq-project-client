package br.edu.fafic.barberproject.main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 */
public class Cliente1 {


    public static void main(String[] args) throws IOException, TimeoutException {
        Agendamento ag = new Agendamento("Rene");
        ag.excute(ag);
    }
}




