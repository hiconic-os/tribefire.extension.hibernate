package tribefire.extension.hibernate.graphfetching.sql.source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelType;

public interface ResultValueExtractor {
	static ResultValueExtractor STRING = ResultSet::getString; 
	static ResultValueExtractor DECIMAL = ResultSet::getBigDecimal;
	static ResultValueExtractor OBJECT = ResultSet::getObject;
	static ResultValueExtractor BOOLEAN = new PrimitiveResultValueExtractor(ResultSet::getBoolean);
	static ResultValueExtractor INTEGER = new PrimitiveResultValueExtractor(ResultSet::getInt);
	static ResultValueExtractor LONG = new PrimitiveResultValueExtractor(ResultSet::getLong);
	static ResultValueExtractor FLOAT = new PrimitiveResultValueExtractor(ResultSet::getFloat);
	static ResultValueExtractor DOUBLE = new PrimitiveResultValueExtractor(ResultSet::getDouble);

	static ResultValueExtractor DATE = (rs, i) -> {
		Timestamp timestamp = rs.getTimestamp(i);
		return timestamp != null? new Date(timestamp.getTime()): null;
	};
	
	Object getValue(ResultSet rs, int i) throws SQLException;
	
	static ResultValueExtractor get(GenericModelType type) {
		switch (type.getTypeCode()) {
		case dateType: return DATE;
		case booleanType: return BOOLEAN;
		case integerType: return INTEGER;
		case longType: return LONG;
		case floatType: return FLOAT;
		case doubleType: return DOUBLE;
		case decimalType: return DECIMAL;
		case stringType: return STRING; 
		case objectType: return OBJECT;
		
		case enumType:
			EnumType<?> enumType = (EnumType<?>)type;
			return (rs, i) -> {
				String strValue = rs.getString(i);
				if (strValue == null)
					return null;
				return enumType.getEnumValue(strValue);
			};
			
		default:
			// can never happen
			throw new IllegalStateException("Type confusion");
		}
	}
	
	class PrimitiveResultValueExtractor implements ResultValueExtractor {
		private final ResultValueExtractor primitiveDelegate;
		
		public PrimitiveResultValueExtractor(ResultValueExtractor primitiveDelegate) {
			super();
			this.primitiveDelegate = primitiveDelegate;
		}

		@Override
		public Object getValue(ResultSet rs, int i) throws SQLException {
			Object v = primitiveDelegate.getValue(rs, i);
			return rs.wasNull()? null: v;
		}
	}

}