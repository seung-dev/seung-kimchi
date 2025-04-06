package seung.kimchi.types.ncloud;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import seung.kimchi.types.SType;

@Builder
@AllArgsConstructor
@Getter
public class SNcloudMessageBody extends SType {

	@NotBlank
	@JsonProperty
	private final String type;
	
	@NotBlank
	@JsonProperty
	private final String contentType;
	
	@JsonProperty
	private final String countryCode;
	
	@NotBlank
	@JsonProperty
	private final String from;
	
	@JsonProperty
	private final String subject;
	
	@JsonProperty
	private final String content;
	
	@NotNull
	@JsonProperty
	private final List<SNcloudMessage> messages;
	
	@JsonProperty
	private final List<SNcloudMessageFile> files;
	
	@JsonProperty
	private final String reserveTime;
	
	@JsonProperty
	private final String reserveTimeZone;
	
	public static class SNcloudMessageBodyBuilder {
		public SNcloudMessageBodyBuilder to(String no) {
			this.messages = Arrays.asList(SNcloudMessage.builder()
					.to(no)
					.build()
					);
			return this;
		}
		public SNcloudMessageBodyBuilder to(List<String> numbers) {
			if(messages != null) {
				this.messages = numbers.stream()
						.map(no -> SNcloudMessage.builder()
								.to(no)
								.build()
								)
						.collect(Collectors.toList());
			}
			return this;
		}
	}
	
}
