package me.m0dex.funquiz.updater;

public class Version {

	private int code;
	private int feature;
	private int fix;
	
	public Version(int _code, int _feature, int _fix) {
		
		code = _code;
		feature = _feature;
		fix = _fix;
		
	}
	
	public boolean isNewerThan(Version other) {
		
		if(this.code > other.getCode())
			return true;
		else if(this.code == other.getCode()) {
			
			if(this.feature > other.getFeature())
				return true;
			else if(this.feature == other.getFeature()) {
				return this.fix > other.getFix();
			}
		}
		
		return false;
	}
	
	public int getCode() { return code; }
	public int getFeature() { return feature; }
	public int getFix() { return fix; }
	
	@Override
	public String toString() { return this.code + "." + this.feature + "." + this.fix; }
	
	public static Version fromString(String versionString) {
		
		versionString = versionString.replace('.', '_');
		
		int _code = Integer.parseInt(versionString.split("_")[0]);
		int _feature = Integer.parseInt(versionString.split("_")[1]);
		int _fix = Integer.parseInt(versionString.split("_")[2]);
		
		return new Version(_code, _feature, _fix);
		
	}
}
