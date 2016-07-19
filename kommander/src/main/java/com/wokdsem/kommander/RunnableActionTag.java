package com.wokdsem.kommander;

class RunnableActionTag {

	private final long superId;
	private final String kommandTag;

	RunnableActionTag(long superId, String kommandTag) {
		this.superId = superId;
		this.kommandTag = kommandTag;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof RunnableActionTag) {
			RunnableActionTag o = (RunnableActionTag) obj;
			if (superId == o.superId) {
				return kommandTag == null ?
						o.kommandTag == null : kommandTag.equals(o.kommandTag);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = (int) (superId ^ (superId >>> 32));
		result = 31 * result + (kommandTag != null ? kommandTag.hashCode() : 0);
		return result;
	}

}
