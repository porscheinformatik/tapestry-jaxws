package org.apache.tapestry.jaxws.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SOAP logging handler which shortens element contents to 100 characters (to keep logs shorter). 
 * <p>
 * Logs SOAP message to logger with given name. Logs SOAP Faults as level WARN, all other messages
 * with level INFO.
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext>
{
	private final Logger logger;

	private final TransformerFactory transformerFactory;

	private static enum LogLevel
	{
		INFO,
		WARN
	};

	/**
	 * Creates a SOAP logging handle for given logger.
	 * 
	 * @param loggerName
	 *            the name of the logger
	 */
	public SOAPLoggingHandler(String loggerName)
	{
		logger = LoggerFactory.getLogger(loggerName);
		transformerFactory = TransformerFactory.newInstance();
	}

	public Set<QName> getHeaders()
	{
		return null;
	}

	public void close(MessageContext context)
	{
		// ignore
	}

	public boolean handleFault(SOAPMessageContext context)
	{
		logMessage(context, LogLevel.WARN);
		return true;
	}

	public boolean handleMessage(SOAPMessageContext context)
	{
		if (logger.isInfoEnabled())
		{
			logMessage(context, LogLevel.INFO);
		}
		return true;
	}

	private void logMessage(SOAPMessageContext smc, LogLevel logLevel)
	{
		try
		{
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(
				SOAPLoggingHandler.class.getResourceAsStream("TruncateLongTextNodes.xsl")));

			Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PrintWriter out = new PrintWriter(buffer);
			SOAPMessage message = smc.getMessage();
			try
			{
				StreamResult streamResult = new StreamResult(out);
				transformer.transform(new DOMSource(message.getSOAPPart().getEnvelope()), streamResult);
			}
			catch (Exception e)
			{
				out.println("Exception in handler: " + e);
			}

			String messageId = getOrCreateMessageId(smc);
			if (outboundProperty.booleanValue())
			{
				log(logLevel, "Outbound message [" + messageId + "]: " + buffer.toString("UTF-8"));
			}
			else
			{
				log(logLevel, "Inbound message: [" + messageId + "]: " + buffer.toString("UTF-8"));
			}
		}
		catch (Exception e)
		{
			// ignore
		}
	}

	private void log(LogLevel logLevel, String message)
	{
		if (logLevel == LogLevel.INFO && logger.isInfoEnabled())
		{
			logger.info(message);
		}
		else
		{
			logger.warn(message);
		}
	}

	private String getOrCreateMessageId(SOAPMessageContext smc)
	{
		String messageId = (String) smc.get("MessageID");
		if (messageId == null)
		{
			messageId = UUID.randomUUID().toString();
			smc.put("MessageID", messageId);
		}
		return messageId;
	}

}
