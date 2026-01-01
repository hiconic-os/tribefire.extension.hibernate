// ============================================================================
package test.models.index;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface IndexedEntity extends GenericEntity {

	EntityType<IndexedEntity> T = EntityTypes.T(IndexedEntity.class);

	String getStr();
	void setStr(String str);

	Set<String> getStrSet();
	void setStrSet(Set<String> strSet);

	List<String> getStrList();
	void setStrList(List<String> strList);

	Map<String, String> getStrStrMap();
	void setStrStrMap(Map<String, String> strMap);

}
