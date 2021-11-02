package br.edu.fafic.barberproject.main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Cliente2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Agendamento ag = new Agendamento("João");
        ag.excute(ag);
    }
}