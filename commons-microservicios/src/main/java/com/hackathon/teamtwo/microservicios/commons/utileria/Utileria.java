package com.hackathon.teamtwo.microservicios.commons.utileria;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

public class Utileria {
	public static String guardarImagenPlus(MultipartFile multiPart, HttpServletRequest request, String ruta) {
		// Obtenemos el nombre original del archivo
		String nombreOriginal = multiPart.getOriginalFilename();
		nombreOriginal = nombreOriginal.replace(" ", "-");
		String nombreFinal = randomAlphaNumeric(8) + nombreOriginal;
		// concatenar la linea y los padres del usuario
		// Obtenemos la ruta ABSOLUTA del directorio images
		// apache-tomcat/webapps/cineapp/resources/images/
		// String rutaFinal =
		// request.getServletContext().getRealPath("/resources/images/");
		String rutaFinal = request.getServletContext().getRealPath(ruta);

		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro

			File creardirectorio = new File(rutaFinal);
			creardirectorio.mkdirs();
			File imageFile = new File(rutaFinal + nombreFinal);

			System.out.println("Mostrar Path Absoluto: " + imageFile.getAbsolutePath());
			System.out.println("Generar Ruta Absulta: " + imageFile.mkdirs());
			System.out.println("Mostrar la Ruta" + imageFile.getPath());
			// Aqui se guarda fisicamente el archivo en el disco duro

			multiPart.transferTo(imageFile);

			BufferedImage originalImage = ImageIO.read(imageFile);
			BufferedImage outputImage = resizeImage(originalImage, 200, 200);
			ImageIO.write(outputImage, "jpg", imageFile);

			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}
	
	public static String randomAlphaNumeric(int count) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}
	
	   public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        Thumbnails.of(originalImage)
	            .size(targetWidth, targetHeight)
	            .outputFormat("jpg")
	            .outputQuality(0.90)
	            .toOutputStream(outputStream);
	        byte[] data = outputStream.toByteArray();
	        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
	        return ImageIO.read(inputStream);
	    }
	
}
