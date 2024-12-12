package seung.kimchi;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import seung.kimchi.types.SLinkedHashMap;

public class SSaxHandler extends DefaultHandler {

	private List<SLinkedHashMap> rows;
	
	private SLinkedHashMap row;
	
	private String tag_name;
	
	private StringBuilder text;
	
	private boolean target;
	
	public SSaxHandler(
			String tag_name
			) {
		rows = new ArrayList<>();
		this.tag_name = tag_name;
	}
	
	public List<SLinkedHashMap> rows() {
		return this.rows;
	}// end of rows
	
	@Override
	public void startDocument() throws SAXException {
		this.target = false;
		this.text = new StringBuilder();
	}// end of startDocument
	
	@Override
	public void startElement(
			String uri
			, String localName
			, String qName
			, Attributes attributes
			) throws SAXException {
		
		if(qName.equals(tag_name)) {
			this.row = new SLinkedHashMap();
			this.target = true;
		}
		
		if(this.target) {
			this.text.setLength(0);
		}
		
	}// end of startElement
	
	@Override
	public void endElement(
			String uri
			, String localName
			, String qName
			) throws SAXException {
		
		if(qName.equals(tag_name)) {
			rows.add(row);
			this.target = false;
		}
		
		if(this.target) {
			row.add(qName, SText.trim(this.text.toString()));
		}
		
	}// end of endElement
	
	@Override
	public void characters(
			char[] ch
			, int start
			, int length
			) throws SAXException {
		this.text = this.text.append(new String(ch, start, length));
	}// end of characters
	
}
