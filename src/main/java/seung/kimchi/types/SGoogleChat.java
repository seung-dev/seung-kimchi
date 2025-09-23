package seung.kimchi.types;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import seung.kimchi.core.SText;
import seung.kimchi.core.types.SException;
import seung.kimchi.core.types.SLinkedHashMap;

public class SGoogleChat {

	private String text;
	
	private String thread_key;
	
	private List<String> card_headers = new ArrayList<>();
	
	private List<List<String>> card_sections = new ArrayList<>();
	
	@Builder
	public SGoogleChat(String text, String thread_key) {
		this.text = text;
		this.thread_key = thread_key;
	}
	
	private String card_header(int index) {
		String header = "";
		if(card_headers.size() >= index + 1) {
			header = card_headers.get(index);
		}
		return header;
	}// end of card_header
	
	private List<SLinkedHashMap> card_widget(List<String> rows) {
		List<SLinkedHashMap> widget = new ArrayList<>();
		widget.add(new SLinkedHashMap()
				.add("textParagraph", new SLinkedHashMap()
						.add("text", String.join(SText._S_LINE_SEPARATOR, rows))
						)
				);
		return widget;
	}// end of card_widget
	
	public String stringify() throws SException {
		
		SLinkedHashMap json = new SLinkedHashMap();
		
		json.add("text", text);
		
		if(thread_key != null && !"".equals(thread_key)) {
			json.add("thread", new SLinkedHashMap().add("threadKey", thread_key));
		}
		
		if(card_sections.size() == 0) {
			return json.stringify();
		}
		
		List<SLinkedHashMap> sections = new ArrayList<>();
		
		SLinkedHashMap section = null;
		String header = "";
		for(int section_no = 0; section_no < card_sections.size(); section_no++) {
			section = new SLinkedHashMap();
			header = card_header(section_no);
			if(!"".equals(header)) {
				section.add("header", header);
			}
			section.add("widgets", card_widget(card_sections.get(section_no)));
			sections.add(section);
		}// end of card_rows
		
		List<SLinkedHashMap> cardsV2 = new ArrayList<>();
		cardsV2.add(new SLinkedHashMap()
				.add("card", new SLinkedHashMap()
						.add("sections", sections)
						)
				);
		
		json.add("cardsV2", cardsV2);
		
		return json.stringify();
	}// end of stringify
	
	private List<String> section(
			int section_no
			) {
		if(card_sections.size() < section_no + 1) {
			List<String> section = new ArrayList<>();
			card_sections.add(section);
		}
		return card_sections.get(section_no);
	}// end of section
	
	public SGoogleChat add_header_in_card(
			int section_no
			, String text
			, String color
			) {
		int loop_size = section_no + 1 - card_headers.size();
		for(int i = 0; i < loop_size; i++) {
			card_headers.add("");
		}
		card_headers.set(section_no, String.format("<b><font color=\"%s\">%s</font></b>", color, text));
		return this;
	}// end of add_header_in_card
	
	public SGoogleChat add_header_in_card(
			int section_no
			, String text
			) {
		add_header_in_card(section_no, text, "#999999");
		return this;
	}// end of add_header_in_card
	
	public SGoogleChat add_row_in_card(
			int section_no
			, String row
			, String header
			) {
		section(section_no).add(row);
		add_header_in_card(section_no, header);
		return this;
	}// end of add_row_in_card
	
	public SGoogleChat add_row_in_card(
			int section_no
			, String... text
			) {
		section(section_no).add(String.join(" ", text));
		return this;
	}// end of add_row_in_card
	
	public SGoogleChat add_row_in_card(
			String... text
			) {
		add_row_in_card(0, text);
		return this;
	}// end of add_row_in_card
	
	public SGoogleChat add_link_in_card(
			int section_no
			, String url_encoded
			, String text
			) {
		add_row_in_card(section_no, String.format("<a href=%s>%s</a>", url_encoded, text));
		return this;
	}// end of add_link_in_card
	
	public SGoogleChat add_link_in_card(
			String url_encoded
			, String text
			) {
		add_link_in_card(0, url_encoded, text);
		return this;
	}// end of add_link_in_card
	
	public SGoogleChat add_line_in_card(
			int section_no
			) {
		section(section_no).add("");
		return this;
	}// end of add_line_in_card
	
	public SGoogleChat add_line_in_card() {
		add_line_in_card(0);
		return this;
	}// end of add_line_in_card
	
}
