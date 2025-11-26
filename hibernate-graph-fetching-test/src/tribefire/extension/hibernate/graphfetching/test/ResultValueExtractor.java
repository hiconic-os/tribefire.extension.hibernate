package tribefire.extension.hibernate.graphfetching.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelType;

public interface ResultValueExtractor {

	Object getValue(ResultSet rs, int i) throws SQLException;
	
	static ResultValueExtractor get(GenericModelType type) {
		switch (type.getTypeCode()) {
		case booleanType: return (rs, i) -> rs.getBoolean(i);
		case dateType: return (rs, i) -> rs.getDate(i);
		case integerType: return (rs, i) -> rs.getInt(i);
		case longType: return (rs, i) -> rs.getLong(i);
		case floatType: return (rs, i) -> rs.getFloat(i);
		case doubleType: return (rs, i) -> rs.getDouble(i);
		case decimalType: return (rs, i) -> rs.getBigDecimal(i);
		case enumType:
			EnumType<?> enumType = (EnumType<?>)type;
			return (rs, i) -> enumType.getEnumValue(rs.getString(i));
		case stringType: return (rs, i) -> rs.getString(i);
		case objectType: return (rs, i) -> rs.getObject(i);
		default:
			// can never happen
			throw new IllegalStateException("Type confusion");
		}
	}
}
