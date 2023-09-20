package seung.kimchi.types;

public enum SCertificatePolicies {

	OID_1_2_410_200005_1_1_1("1.2.410.200005.1.1.1", "yessign", "금융결제원", "범용", "개인")
	, OID_1_2_410_200005_1_1_1_10("1.2.410.200005.1.1.4.7", "yessign", "금융결제원", "금융", "개인")
	, OID_1_2_410_200005_1_1_4("1.2.410.200005.1.1.4", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_1("1.2.410.200005.1.1.4.1", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_2("1.2.410.200005.1.1.4.2", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_3("1.2.410.200005.1.1.4.3", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_4("1.2.410.200005.1.1.4.4", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_5("1.2.410.200005.1.1.4.5", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200005_1_1_4_6("1.2.410.200005.1.1.4.6", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200004_5_1_1_9("1.2.410.200004.5.1.1.9", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200004_5_2_1_2("1.2.410.200004.5.2.1.2", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200004_5_2_1_7_1("1.2.410.200004.5.2.1.7.1", "yessign", "금융결제원", "은행용", "개인")
	, OID_1_2_410_200004_5_4_1_1("1.2.410.200004.5.4.1.1", "yessign", "금융결제원", "은행용", "개인")
	;
	
	private final String id;
	public String id() {
		return this.id;
	}
	
	private final String issuer;
	public String issuer() {
		return this.issuer;
	}
	
	private final String issuer_kr;
	public String issuer_kr() {
		return this.issuer_kr;
	}
	
	private final String usage;
	public String usage() {
		return this.usage;
	}
	
	private final String subject;
	public String subject() {
		return this.subject;
	}
	
	private SCertificatePolicies(String id, String issuer, String issuer_kr, String usage, String subject) {
		this.id = id;
		this.issuer = issuer;
		this.issuer_kr = issuer_kr;
		this.usage = usage;
		this.subject = subject;
	}
	
	public static SCertificatePolicies resolve(String id) {
		for(SCertificatePolicies policy : values()) {
			if(policy.id.equals(id)) {
				return policy;
			}
		}
		return null;
	}// end of resolve
	
}
