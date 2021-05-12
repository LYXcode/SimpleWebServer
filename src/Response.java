import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	private static final int BUFFER_SIZE = 1024;
	Request request;
	OutputStream output;

	public Response(OutputStream output) {
		this.output = output;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	public void sendStaticResource() throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		String fileName = request.getUri();
		try {
			File file = new File(HttpSever.WEB_ROOT, request.getUri());
			if(file.exists()) {
				fis = new FileInputStream(file);
				int ch = fis.read(bytes, 0, BUFFER_SIZE);
				String preString = "HTTP/1.1 200\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + file.length() + "\r\n" + "\r\n";
				output.write(preString.getBytes());
				while(ch != -1) {
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);
				}
			}
			else {
				String errorMessageString = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 23\r\n" + "\r\n" + "<h1>" + fileName+ " Not Found</h1>";
				output.write(errorMessageString.getBytes());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		finally {
			if(fis != null) {
				fis.close();
			}
		}
	}

}
