package com.drozal.dataterminal.logs.Impound;

import com.drozal.dataterminal.util.Misc.stringUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

@XmlRootElement
public class ImpoundReportLogs {
	private List<ImpoundLogEntry> logs;
	
	public ImpoundReportLogs() {
	}
	
	public static List<ImpoundLogEntry> extractLogEntries(String filePath) {
		List<ImpoundLogEntry> logEntries = new ArrayList<>();
		
		try {
			File file = new File(filePath);
			
			if (!file.exists()) {
				return logEntries;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement()
			   .normalize();
			
			NodeList logsList = doc.getElementsByTagName("logs");
			
			for (int i = 0; i < logsList.getLength(); i++) {
				Node logsNode = logsList.item(i);
				if (logsNode.getNodeType() == Node.ELEMENT_NODE) {
					Element logsElement = (Element) logsNode;
					ImpoundLogEntry logEntry = new ImpoundLogEntry();
					logEntry.impoundNumber = getTagValue(logsElement, "impoundNumber");
					logEntry.impoundDate = getTagValue(logsElement, "impoundDate");
					logEntry.impoundTime = getTagValue(logsElement, "impoundTime");
					logEntry.ownerName = getTagValue(logsElement, "ownerName");
					logEntry.ownerAge = getTagValue(logsElement, "ownerAge");
					logEntry.ownerGender = getTagValue(logsElement, "ownerGender");
					logEntry.ownerAddress = getTagValue(logsElement, "ownerAddress");
					logEntry.impoundPlateNumber = getTagValue(logsElement, "impoundPlateNumber");
					logEntry.impoundModel = getTagValue(logsElement, "impoundModel");
					logEntry.impoundType = getTagValue(logsElement, "impoundType");
					logEntry.impoundColor = getTagValue(logsElement, "impoundColor");
					logEntry.impoundComments = getTagValue(logsElement, "impoundComments");
					logEntry.officerRank = getTagValue(logsElement, "officerRank");
					logEntry.officerName = getTagValue(logsElement, "officerName");
					logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
					logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
					logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
					logEntries.add(logEntry);
				}
			}
		} catch (Exception e) {
			logError("Exception", e);
		}
		return logEntries;
	}
	
	public static String getTagValue(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0)
			               .getTextContent();
		}
		return "";
	}
	
	public static int countReports() {
		try {
			
			List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static List<ImpoundLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.impoundLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(ImpoundReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ImpoundReportLogs logList = (ImpoundReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	public static void saveLogsToXML(List<ImpoundLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ImpoundReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ImpoundReportLogs logList = new ImpoundReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.impoundLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("Exception", e);
		}
	}
	
	public List<ImpoundLogEntry> getLogs() {
		return logs;
	}
	
	public void setLogs(List<ImpoundLogEntry> logs) {
		this.logs = logs;
	}
	
}