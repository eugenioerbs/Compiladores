import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Arquivo {

    public Arquivo() {

    }// construtor

    public String ler(String caminho) throws Exception {
        String conteudo = "";
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha = leitor.readLine();

            while (linha != null) {
                conteudo = conteudo + linha + "\n";
                linha = leitor.readLine();

                if (linha != null) {
                    conteudo += "\n";
                }
            }

        } catch (Exception erro) {
            throw erro;
        }

        return conteudo;
    }

    
    public void salvar(String caminho, String conteudo) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminho))) {
            escritor.write(conteudo);
        } catch (IOException erro) {
            throw erro;
        }
    }

}// class
