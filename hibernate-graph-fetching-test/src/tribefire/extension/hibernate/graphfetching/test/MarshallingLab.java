package tribefire.extension.hibernate.graphfetching.test;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.gm.graphfetching.test.model.tech.Entitya;
import com.braintribe.gm.graphfetching.test.model.tech.Entityb;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.pr.AbsenceInformation;
import com.braintribe.model.generic.pr.AbsentEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.VdHolder;

public class MarshallingLab {
	public static void main(String[] args) {
		Entitya entity = Entitya.T.create();
		Entitya entity2 = Entitya.T.create();
		
		AbsentEntity absentEntity = AbsentEntity.create(Entityb.T, 1L);
		
		Property property = Entitya.T.getProperty(Entitya.entityb);
		property.setAbsenceInformation(entity, absentEntity);
		
		property.setAbsenceInformation(entity2, GMF.absenceInformation());
		
		JsonStreamMarshaller marshaller = new JsonStreamMarshaller();
		marshaller.marshall(System.out, entity);
		System.out.println();
		marshaller.marshall(System.out, entity2);
		System.out.println();
	}
}
