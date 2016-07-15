package com.wokdsem.kommander;

class KommandTag {

	public final long kommanderId;
	public final String kommandTag;

	KommandTag(long kommanderId, String kommandTag) {
		this.kommanderId = kommanderId;
		this.kommandTag = kommandTag;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof KommandTag) {
			KommandTag o = (KommandTag) obj;
			if (kommanderId == o.kommanderId) {
				return kommandTag == null ?
						o.kommandTag == null : kommandTag.equals(o.kommandTag);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = (int) (kommanderId ^ (kommanderId >>> 32));
		result = 31 * result + (kommandTag != null ? kommandTag.hashCode() : 0);
		return result;
	}

}
