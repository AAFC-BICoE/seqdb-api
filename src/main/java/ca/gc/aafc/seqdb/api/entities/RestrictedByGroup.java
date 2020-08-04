package ca.gc.aafc.seqdb.api.entities;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.gc.aafc.seqdb.api.entities.Group;

/**
 * "getAccessGroup()" Specifies the Group that the user should be a member of to access the instance of the Entity that implements this interface.
 */
public interface RestrictedByGroup {
	@Transient
	@JsonIgnore
	public Group getAccessGroup();
}
