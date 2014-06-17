package pl.cyfronet.ismop.flume.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import pl.cyfronet.ismop.flume.events.MomEvent.Builder;

public class MomEncoderDecoder {

	public static void main(String[] args) throws IOException {

		Builder newBuilder = MomEvent.newBuilder();

		newBuilder.setValue(123);
		newBuilder.setTimestamp(new Date().getTime());
		newBuilder.setSensorId("12321");
		newBuilder.setMonitoringStationId("12321");
		newBuilder.setMomTopicName("topicName");

		MomEvent event = newBuilder.build();

		MomEncoderDecoder ed = new MomEncoderDecoder();
		
		byte[] byteArray = ed.encode(event);

		MomEvent decoded = ed.decode(byteArray);

		System.out.println(decoded);
	}

	public MomEvent decode(byte[] byteArray) throws IOException {
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

		JsonDecoder jsonDecoder = new DecoderFactory().jsonDecoder(
				MomEvent.getClassSchema(), inputStream);

		SpecificDatumReader<MomEvent> datumReader = new SpecificDatumReader<MomEvent>(
				MomEvent.class);

		MomEvent reuse = new MomEvent();
		datumReader.read(reuse, jsonDecoder);

		return reuse;
	}

	public byte[] encode(MomEvent event) throws IOException {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		JsonEncoder jsonEncoder = new EncoderFactory().jsonEncoder(
				MomEvent.getClassSchema(), stream);

		SpecificDatumWriter<MomEvent> datumWriter = new SpecificDatumWriter<MomEvent>(
				MomEvent.class);
		
		datumWriter.write(event, jsonEncoder);
		jsonEncoder.flush();

		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

}
