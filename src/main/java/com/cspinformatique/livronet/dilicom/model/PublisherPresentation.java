package com.cspinformatique.livronet.dilicom.model;

public enum PublisherPresentation {
	HARDBACK("R", "Relié"),
	PAPER_BACKED("B", "Broché"),
	POCKET("P", "Poche"),
	GAME("J", "Jeu"),
	VINYL("D", "Disque vinyle"),
	COMPACT_DISC("DC", "CD"),
	DVD("DV", "DVD"),
	CD("CD", "Cédérom"),
	RECORD_BOOK("LD", "Livre disque"),
	TAPE("K", "Cassette"),
	AUDIO_TAPE("KA", "Cassette audio"),
	VIDEO_TAPE("KV", "Cassette vidéo"),
	TAPE_BOOK("LK", "Livre cassette"),
	LEATHER("C", "Cuir"),
	CASE("E", "Etui"),
	LUXURY("L", "Luxe"),
	PAPER("X", "Journal, revue"),
	MAGNETIC("SM", "Support magnétique"),
	SLIDES("DI", "Diapositives"),
	ADVERTISING("PC", "Publicité"),
	ALBULM("AL", "Album"),
	ROADMAPS("CR", "Cartes routières"),
	POSTERS("PO", "Posters"),
	CALENDARS("CA", "Calendriers"),
	OBJECT("O", "Obect"),
	NUMERIC("N", "Contenu numérique");
	
	private String code;
	private String description;
	
	PublisherPresentation(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		return this.description;
	}
}
