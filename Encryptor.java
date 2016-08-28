import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
 
public class Encryptor
{
	public static byte[] toBytes(String fileInput){
		FileInputStream fileInputStream=null;	 
        File file = new File(fileInput);
        byte[] bFile = new byte[(int) file.length()];
        try {
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    System.out.println("Done");
        }catch(Exception e){
        	e.printStackTrace();
        }
        return bFile;
	}
	
	public static byte[] encrypt(byte[] fileInput, String codeWord){
		if (codeWord.equals("")){
			codeWord="password";
		}
		for (int i = 0; i < fileInput.length; i++) {
	       		fileInput[i] += (int) codeWord.charAt(i%codeWord.length());;
            }
		return fileInput;
	}
	
	public static byte[] decrypt(byte[] fileInput, String codeWord){
		if (codeWord.equals("")){
			codeWord="password";
		}
		for (int i = 0; i < fileInput.length; i++) {
       		fileInput[i] -= (int) codeWord.charAt(i%codeWord.length());
        }
		return fileInput;
	}
	
	public static void fileFromBytes(byte[] fileInput, String command, String fileName) throws IOException{
		if(command.equals("ENCRYPT")){
			FileOutputStream stream = new FileOutputStream(fileName +".ecm");
			try {
			    stream.write(fileInput);
			    System.out.println("wrote it");
			} finally {
			    stream.close();
			}
		}
		else{
			FileOutputStream stream = new FileOutputStream(fileName.substring(0, fileName.length()-4));
			try {
			    stream.write(fileInput);
			} finally {
			    stream.close();
			}
		}
	}
	
    public static void main( String[] args ) throws IOException
    {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("File Encryptor & Decryptor");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
		Font font = new Font("Verdana", Font.PLAIN, 24);
		//frame.getContentPane().setLayout(boxLayout);
		frame.setLayout(new GridLayout(3, 3));
		JLabel encrT = new JLabel("Drop file to be encrypted here:", JLabel.CENTER);
		final JTextField encr = new JTextField("");
		encr.setEditable(false);
		JTextField encrLoc = new JTextField("");
		encr.setHorizontalAlignment(JTextField.CENTER);
		JLabel decrT = new JLabel("Drop file to be decrypted here:", JLabel.CENTER);
		final JTextField decr = new JTextField("");
		decr.setEditable(false);
		JTextField decrLoc = new JTextField("");
		decr.setHorizontalAlignment(JTextField.CENTER);
		JLabel cdwrdLabel = new JLabel("<---- Codeword", JLabel.CENTER);
		JLabel cdwrdLabel1 = new JLabel("Codeword ---->", JLabel.CENTER);
		JPasswordField cdwrd = new JPasswordField();
		cdwrd.setHorizontalAlignment(JPasswordField.CENTER);
		JButton encrB = new JButton("Encrypt!");
		JButton decrB = new JButton("Decrypt!");
		
		
		class encrListener implements ActionListener
        {
    	    public void actionPerformed(ActionEvent event)
    	    {
    	    	String pwd = new String(cdwrd.getPassword());
    	    	String filename = new String(encrLoc.getText());
    	    	try {
					fileFromBytes(encrypt(toBytes(filename),pwd),"ENCRYPT",filename);
					encr.setText("Encrypted!");
					cdwrd.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
        }
		
		class decrListener implements ActionListener
        {
    	    public void actionPerformed(ActionEvent event)
    	    {
    	    	String pwd = new String(cdwrd.getPassword());
    	    	String filename = new String(decrLoc.getText());
    	    	try {
					fileFromBytes(decrypt(toBytes(filename),pwd),"DECRYPT",filename);
					decr.setText("Decrypted!");
					cdwrd.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
        }
		
		encrListener listener1 = new encrListener();
    	encrB.setFont(font);
    	encrB.addActionListener(listener1);
    	decrListener listener2 = new decrListener();
    	decrB.setFont(font);
    	decrB.addActionListener(listener2);
    	
    	cdwrdLabel.setFont(new Font("Times", Font.BOLD, 16));
    	cdwrdLabel.setBackground(Color.black);
    	cdwrdLabel.setForeground(Color.WHITE);
    	cdwrdLabel.setOpaque(true);
    	
    	cdwrdLabel1.setFont(new Font("Times", Font.BOLD, 16));
    	cdwrdLabel1.setBackground(Color.black);
    	cdwrdLabel1.setForeground(Color.WHITE);
    	cdwrdLabel1.setOpaque(true);
    	
    	encrT.setFont(new Font("Times", Font.BOLD, 16));
    	encrT.setBackground(Color.yellow);
    	encrT.setForeground(Color.RED);
    	encrT.setOpaque(true);
    	
    	decrT.setFont(new Font("Times", Font.BOLD, 16));
    	decrT.setBackground(Color.red);
    	decrT.setForeground(Color.YELLOW);
    	decrT.setOpaque(true);
    	

        encr.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        encrLoc.setText(file.getAbsolutePath());
                    	encr.setText("File ready!");
                    	decr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        decr.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        decrLoc.setText(file.getAbsolutePath());
                    	decr.setText("File ready!");
                    	encr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    	
    	frame.add(cdwrdLabel1);
    	frame.add(cdwrd);
    	frame.add(cdwrdLabel);
    	
    	frame.add(encrT);
    	frame.add(encr);
    	frame.add(encrB);
    	
    	frame.add(decrT);
    	frame.add(decr);
    	frame.add(decrB);
    	
    	frame.setSize(3*(7*(screenSize.height)/10)/4,screenSize.height/2);
    	frame.setVisible(true);	
    }
}