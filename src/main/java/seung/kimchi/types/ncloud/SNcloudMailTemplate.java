package seung.kimchi.types.ncloud;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import seung.kimchi.SNcloud;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SType;

@Builder
@AllArgsConstructor
@Getter
public class SNcloudMailTemplate extends SType {

	@Builder.Default
	@JsonProperty
	private final boolean advertising = false;
	
	@NotBlank
	@JsonProperty
	private final String templateSid;
	
	@NotBlank
	@JsonProperty
	private final String senderAddress;
	
	@NotBlank
	@JsonProperty
	private final String senderName;
	
	@NotBlank
	@JsonProperty
	private final String title;
	
	@NotBlank
	@JsonProperty
	private final List<SNcloudMailRecipient> recipients;
	
	public static class SNcloudMailTemplateBuilder {
		public SNcloudMailTemplateBuilder to(String address, SLinkedHashMap parameters) {
			this.recipients = Arrays.asList(SNcloudMailRecipient.builder()
					.type(SNcloud._S_NCLOUD_TYPE_RECIPIENT)
					.address(address)
					.parameters(parameters)
					.build()
					);
			return this;
		}
		public SNcloudMailTemplateBuilder to(List<String> addresses, SLinkedHashMap parameters) {
			if(addresses != null) {
				this.recipients = addresses.stream()
						.map(address -> SNcloudMailRecipient.builder()
								.address(address)
								.parameters(parameters)
								.build()
								)
						.collect(Collectors.toList());
			}
			return this;
		}
	}
	
}
