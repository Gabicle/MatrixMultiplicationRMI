import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.Naming;
import java.util.StringTokenizer;

public class Client {
    private MatrixCalculator matrixCalculator;

public void getCalcFromServer(){
    try{
        matrixCalculator = (MatrixCalculator) Naming.lookup("rmi://localhost:1099/MatrixCalculatorService");
        System.out.println("Got Calculator from Server");
    } catch (Exception e){
        e.printStackTrace();

    }
}

public Matrix multiply(Matrix a, double b) throws Exception{
    return matrixCalculator.multiply(a,b);
}

public Matrix multiply(Matrix a, Matrix b) throws Exception{
    if (a.getColumn()!=b.getRow()){
        throw  new Exception("Size Mismatch");
    }
    return matrixCalculator.multiply(a, b);
}

    public static void main(String[] args) {
        Client client = new Client();
        client.getCalcFromServer();
        JFrame jFrame = client.new ClientFrame();
        jFrame.setTitle("Matrix Multiplication Calucator");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }

    class ClientFrame extends JFrame{
        private JTextArea matrixA;
        private JTextArea matrixB;
        private JTextArea matrixC;
        private JButton buttonAmulB;
        private JLabel statusLabel;
        private JPanel matricesPane;
        private JPanel buttonsPane;

        public ClientFrame() {
            //INITIALIZE STATUS
            statusLabel = new JLabel("tip:Enter a matrix or number to each textArea.");
            //CREATE MATRICES
            createMatricesPane();
            //CREATE BUTTONS
            createButtonsPane();
            //BIND BUTTONS WITH LISTENERS
            try {
                bindButtonsWithListeners();
            } catch (Exception e) {
                e.printStackTrace();
            }

            setSize(new Dimension(600, 200));
            add(statusLabel, BorderLayout.NORTH);
            add(matricesPane, BorderLayout.CENTER);
            add(buttonsPane, BorderLayout.SOUTH);

            try {
                URL imagUrl = getClass().getResource("Calculator.jpg");
                Image img = new ImageIcon(imagUrl).getImage();
                setIconImage(img);
            } catch (Exception e) {
                System.out.println("turn to /bin/client , then  type java CalculatorClient in the terminal to see the image of this app");
            }
        }

        public Matrix readMatrix(JTextArea matrixArea) throws Exception{
            //DETERMINE THE SIZE VALID AND NOT EMPTY
            String rawText = matrixArea.getText();
            StringTokenizer rowsText = new StringTokenizer(rawText,"\n");

            int rowNum = rowsText.countTokens();

            int[] colomnsNum = new int[rowNum];

            for (int i = 0;i < rowNum;i++) {
                StringTokenizer rowText = new StringTokenizer(rowsText.nextToken());
                colomnsNum[i] = rowText.countTokens();
            }

            if (rowNum==0||colomnsNum[0]==0){
                statusLabel.setText("warning : Can't Find Matrix !");
                throw new Exception("warning : Can't Find Matrix !");
            }

            for (int colomnNum : colomnsNum){
                if (colomnNum != colomnsNum[0]){
                    statusLabel.setText("warning : Invalid Matrix !");
                    throw new Exception("warning : Invalid Matrix !");
                }
            }

            //INITIALIZE THE MATRIX
            int colomnNum = colomnsNum[0];
            Matrix matrix;
            rowsText = new StringTokenizer(rawText,"\n");
            try{
                double[][] values = new double[rowNum][colomnNum];
                for (int i = 0; i < rowNum; i++){
                    StringTokenizer rowText = new StringTokenizer(rowsText.nextToken());
                    for (int j = 0; j < colomnNum; j++){
                        values[i][j] = Double.parseDouble(rowText.nextToken());
                    }
                }
                matrix = new Matrix(values);
            }
            catch (Exception e){
                statusLabel.setText("waring : Invalid Number Format !");
                throw new Exception("waring : Invalid Number Format !");
            }

            return matrix;

        }

        public void displayMatrixToC(Matrix matrixToDisplay){
            StringBuilder rawText = new StringBuilder();
            for (int i = 0; i < matrixToDisplay.getRow(); i++){
                for (int j = 0; j<matrixToDisplay.getColumn(); j++){
                    rawText.append(matrixToDisplay.getVals()[i][j]+"  ");
                }
                rawText.append("\n");
            }
            matrixC.setText(rawText.toString());
        }

        public JPanel matrixPane(JTextArea matrix,String name){
            JScrollPane scrollPane = new JScrollPane(matrix);
            JLabel nameLabel = new JLabel(name);
            scrollPane.setSize(new Dimension(200,200));

            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            pane.add(nameLabel);
            pane.add(scrollPane);

            return pane;
        }

        public void createMatricesPane(){
            //CREATE MATRICES
            matrixA = new JTextArea();
            matrixB = new JTextArea();
            matrixC = new JTextArea();
            JPanel mAPane = matrixPane(matrixA, "Matrix A");
            JPanel mBPane = matrixPane(matrixB, "Matrix B");
            JPanel mCPane = matrixPane(matrixC, "Matrix C");
            matricesPane = new JPanel();
            matricesPane.setLayout(new BoxLayout(matricesPane, BoxLayout.X_AXIS));
            matricesPane.add(mAPane);
            matricesPane.add(mBPane);
            matricesPane.add(mCPane);
        }

        public void createButtonsPane(){
            //CREATE BUTTONS
             buttonAmulB = new JButton("A * B = C");

            buttonsPane = new JPanel();
            buttonsPane.setLayout(new GridLayout(1,3));
            buttonsPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            buttonsPane.add(buttonAmulB);
        }

        public void bindButtonsWithListeners() {

            buttonAmulB.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent e) {
                    try{
                        Matrix a = readMatrix(matrixA);
                        Matrix b = readMatrix(matrixB);

                        int aRow = a.getRow();
                        int aColumn = a.getColumn();
                        int bRow = b.getRow();
                        int bColumn = b.getColumn();

                        if ((aRow+aColumn!=2)&&(bRow+bColumn!=2)){
                            displayMatrixToC(multiply(a, b));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");
                        }
                        else if ((aRow+aColumn==2)&&(bRow+bColumn!=2)){
                            displayMatrixToC(multiply(b,a.getVals()[0][0]));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");
                        }
                        else{
                            displayMatrixToC(multiply(a, b.getVals()[0][0]));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");

                        }


                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        String warning = ex.getMessage();
                        if (warning!=null){
                            statusLabel.setText(warning);
                        }
                    }
                }
            });

        }

        }
}
