package com.demo.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImportExportUtil {

	public byte[] exportToCsv( List<?> anyList , String[] headers) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream));
			writer.writeNext(headers);
			
			  for (Object anyItem : anyList) {
		            String[] data = new String[headers.length];

		            for (int i = 0; i < headers.length; i++) {
		                // Use reflection to get the value of the property based on the header name
		                String header = headers[i];
		                String methodName = "get" + header.substring(0, 1).toUpperCase() + header.substring(1); // Capitalize first letter
		                Method method = anyItem.getClass().getMethod(methodName);
		                Object value = method.invoke(anyItem);
		                data[i] = value != null ? value.toString() : ""; // Handle null values
		            }

		            // Write the data to the CSV
		            writer.writeNext(data);
		        
		         
			  
			  }
		} catch (Exception e) {
			
			System.out.println(e);
			
		
		}
		 return byteArrayOutputStream.toByteArray(); 
	}
	
	
	
	private <T> List<T> extractDataFromCsv(MultipartFile file , T type) {

		log.info("In EmployeeService inside PRIVATE extractDataFromCsv() --Enter");

		try {

			Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<T>();
			strategy.setType((Class<? extends T>) type.getClass());

			CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)

					.withMappingStrategy(strategy).withIgnoreEmptyLine(Boolean.TRUE)
					.withIgnoreLeadingWhiteSpace(Boolean.TRUE).build();

			log.info("In EmployeeService inside PRIVATE extractDataFromCsv() --Exit");

			return csvToBean.parse().stream().toList();

		} catch (Exception e) {
			System.out.println(e);
			log.error("In EmployeeService inside PRIVATE extractDataFromCsv()");

			return null;
		}

	}
	
}
