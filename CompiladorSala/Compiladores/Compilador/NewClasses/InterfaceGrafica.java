import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.event.InputEvent;

public class InterfaceGrafica {
    public static void main(String[] args) {

        // Janela (tamanho; abertura; e fechamento)
        JFrame janela = new JFrame();
        janela.setBounds(0, 0, 1500, 800);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        janela.setLayout(new BorderLayout());

        // Barra de Ferramentas
        JPanel barraFerramentas = new JPanel();
        barraFerramentas.setLayout(new FlowLayout(FlowLayout.LEFT));
        barraFerramentas.setPreferredSize(new Dimension(janela.getWidth(), 70));
        barraFerramentas.setBackground(Color.lightGray);

        // Área de edição de texto
        JTextArea editor = new JTextArea();
        editor.setFont(new Font("Monospaced", Font.PLAIN, 15));
        editor.setBorder(new NumberedBorder());

        JScrollPane scroll = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Área de mensagens
        JTextArea mensagem = new JTextArea();
        mensagem.setFont(new Font("Monospaced", Font.PLAIN, 15));
        mensagem.setEditable(false);
        JScrollPane scrollMensagem = new JScrollPane(mensagem, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Divisória
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, scrollMensagem);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.8);
        janela.add(splitPane, BorderLayout.CENTER);

        // Barra de status
        JPanel barraStatus = new JPanel();
        barraStatus.setLayout(new FlowLayout(FlowLayout.LEFT));
        barraStatus.setPreferredSize(new Dimension(janela.getWidth(), 25));
        barraStatus.setBackground(Color.LIGHT_GRAY);

        // Escrita status
        JLabel statusLabel = new JLabel("Arquivo: nenhum");
        barraStatus.add(statusLabel);

        // Botões
        JButton novo = new JButton("novo [ctrl-n]");
        novo.setPreferredSize(new Dimension(150, 55));
        novo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");
                editor.setText("");
                statusLabel.setText("");
            }
        });

        JButton abrir = new JButton("abrir [ctrl-o]");
        abrir.setPreferredSize(new Dimension(150, 55));
        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");

                JFileChooser seletor = new JFileChooser();

                int respostaSeletor = seletor.showOpenDialog(janela);

                if (respostaSeletor == JFileChooser.APPROVE_OPTION) {

                    File arquivoSelecionado = seletor.getSelectedFile();
                    String caminhoArquivoSelecionado = arquivoSelecionado.getAbsolutePath();
                    try {
                        Arquivo classe = new Arquivo();
                        String conteudoLido = classe.ler(caminhoArquivoSelecionado);
                        editor.setText(conteudoLido);

                        statusLabel.setText("Arquivo: " + caminhoArquivoSelecionado);

                    } catch (Exception ex) {
                        mensagem.setText("Erro ao abrir o arquivo:" + ex.getMessage());
                    }

                } else {

                }

            }
        });

        JButton salvar = new JButton("salvar [ctrl-s]");
        salvar.setPreferredSize(new Dimension(150, 55));
        salvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");

                JFileChooser seletor = new JFileChooser();

                // Chama a caixa de diálogo para salvar
                int respostaSeletor = seletor.showSaveDialog(janela);

                if (respostaSeletor == JFileChooser.APPROVE_OPTION) {
                    File arquivoSelecionado = seletor.getSelectedFile();
                    String caminhoArquivoSelecionado = arquivoSelecionado.getAbsolutePath();

                    try {
                        Arquivo classe = new Arquivo();
                        classe.salvar(caminhoArquivoSelecionado, editor.getText());

                        statusLabel.setText("Arquivo: " + caminhoArquivoSelecionado);
                        mensagem.setText("Arquivo salvo com sucesso!");

                    } catch (Exception ex) {
                        mensagem.setText("Erro ao salvar o arquivo: " + ex.getMessage());
                    }
                }
            }

        });

        JButton copiar = new JButton("copiar [ctrl-c]");
        copiar.setPreferredSize(new Dimension(150, 55));
        copiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");
                editor.copy();
            }
        });

        JButton colar = new JButton("colar [ctrl-v]");
        colar.setPreferredSize(new Dimension(150, 55));
        colar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");
                editor.paste();
            }
        });

        JButton recortar = new JButton("recortar [ctrl-x]");
        recortar.setPreferredSize(new Dimension(150, 55));
        recortar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");
                editor.cut();
            }
        });

        JButton compilar = new JButton("compilar [F7]");
        compilar.setPreferredSize(new Dimension(150, 55));
        compilar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Lexico lexico = new Lexico();
                lexico.setInput(editor.getText());
                try {
                    Token t = null;
                    while ((t = lexico.nextToken()) != null) {
                        System.out.println(t.getLexeme());
                        mensagem.setText(t.getLexeme());
                        // só escreve o lexema, necessário escrever t.getId, t.getPosition()

                        t.getId (); //- retorna o identificador da classe (ver Constants.java)
                        mensagem.setText(t.getId());// necessário adaptar, pois deve ser apresentada a classe por extenso
                        
                        // t.getPosition () - retorna a posição inicial do lexema no editor
                        // necessário adaptar para mostrar a linha

                        // esse código apresenta os tokens enquanto não ocorrer erro
                        // no entanto, os tokens devem ser apresentados SÓ se não ocorrer erro,
                        // necessário adaptar para atender o que foi solicitado
                    }
                } catch (LexicalError erro) { // tratamento de erros
                    System.out.println(erro.getMessage() + " em " + erro.getPosition());
                    mensagem.setText(erro.getMessage() + " em " + erro.getPosition());
                    // e.getMessage() - retorna a mensagem de erro de SCANNER_ERRO (ver
                    // ScannerConstants.java)
                    // necessário adaptar conforme o enunciado da parte 2

                    // e.getPosition() - retorna a posição inicial do erro
                    // necessário adaptar para mostrar a linha
                }

            }
        });

        JButton equipe = new JButton("equipe [F1]");
        equipe.setPreferredSize(new Dimension(150, 55));
        equipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagem.setText("");
                mensagem.setText("Eduarda, Eugênio e Tiago");
            }
        });

        // Adicionando e editando os icones nos botões
        ImageIcon news = new ImageIcon("Images/new.png");
        Image imgNews = news.getImage();
        Image nImgNews = imgNews.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgNewsRedm = new ImageIcon(nImgNews);
        novo.setIcon(nImgNewsRedm);
        novo.setHorizontalTextPosition(SwingConstants.CENTER);
        novo.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon open = new ImageIcon("Images/open.png");
        Image imgOpen = open.getImage();
        Image nImgOpen = imgOpen.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgOpenRedm = new ImageIcon(nImgOpen);
        abrir.setIcon(nImgOpenRedm);
        abrir.setHorizontalTextPosition(SwingConstants.CENTER);
        abrir.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon save = new ImageIcon("Images/save.png");
        Image imgSave = save.getImage();
        Image nImgSave = imgSave.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgSaveRedm = new ImageIcon(nImgSave);
        salvar.setIcon(nImgSaveRedm);
        salvar.setHorizontalTextPosition(SwingConstants.CENTER);
        salvar.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon copy = new ImageIcon("Images/copy.png");
        Image imgCopy = copy.getImage();
        Image nImgCopy = imgCopy.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgCopyRedm = new ImageIcon(nImgCopy);
        copiar.setIcon(nImgCopyRedm);
        copiar.setHorizontalTextPosition(SwingConstants.CENTER);
        copiar.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon paste = new ImageIcon("Images/colar.png");
        Image imgPaste = paste.getImage();
        Image nImgPaste = imgPaste.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgPasteRedm = new ImageIcon(nImgPaste);
        colar.setIcon(nImgPasteRedm);
        colar.setHorizontalTextPosition(SwingConstants.CENTER);
        colar.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon recort = new ImageIcon("Images/recort.png");
        Image imgRecort = recort.getImage();
        Image nImgRecort = imgRecort.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgRecortRedm = new ImageIcon(nImgRecort);
        recortar.setIcon(nImgRecortRedm);
        recortar.setHorizontalTextPosition(SwingConstants.CENTER);
        recortar.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon compile = new ImageIcon("Images/compile.png");
        Image imgCompile = compile.getImage();
        Image nImgCompile = imgCompile.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgCompileRedm = new ImageIcon(nImgCompile);
        compilar.setIcon(nImgCompileRedm);
        compilar.setHorizontalTextPosition(SwingConstants.CENTER);
        compilar.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon grupo = new ImageIcon("Images/gruouptwo.png");
        Image imgGrupo = grupo.getImage();
        Image nImgGrupo = imgGrupo.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nImgGrupoRedm = new ImageIcon(nImgGrupo);
        equipe.setIcon(nImgGrupoRedm);
        equipe.setHorizontalTextPosition(SwingConstants.CENTER);
        equipe.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Adds
        janela.add(barraFerramentas, BorderLayout.NORTH);
        barraFerramentas.add(novo);
        barraFerramentas.add(abrir);
        barraFerramentas.add(salvar);
        barraFerramentas.add(copiar);
        barraFerramentas.add(colar);
        barraFerramentas.add(recortar);
        barraFerramentas.add(compilar);
        barraFerramentas.add(equipe);
        janela.add(barraStatus, BorderLayout.SOUTH);

        // Implementação dos atalhos
        JComponent rootPane = janela.getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "novo");
        rootPane.getActionMap().put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novo.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "abrir");
        rootPane.getActionMap().put("abrir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrir.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        rootPane.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copiar");
        rootPane.getActionMap().put("copiar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiar.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "colar");
        rootPane.getActionMap().put("colar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colar.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "recortar");
        rootPane.getActionMap().put("recortar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recortar.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0),
                "compilar");
        rootPane.getActionMap().put("compilar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compilar.doClick();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
                "equipe");
        rootPane.getActionMap().put("equipe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equipe.doClick();
            }
        });

        // Janela
        janela.setVisible(true);

    }// psvd

}// classe
