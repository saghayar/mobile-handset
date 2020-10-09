package com.axiom.mobilehandset.service.impl;

import com.axiom.mobilehandset.exception.InvalidRequestParameterException;
import com.axiom.mobilehandset.model.MobileHandset;
import com.axiom.mobilehandset.service.IMobileHandsetService;
import com.axiom.mobilehandset.util.Utils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.axiom.mobilehandset.util.Utils.formattedParams;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

@Service
public class MobileHandsetService implements IMobileHandsetService {

    private final MongoTemplate mongoTemplate;

    public MobileHandsetService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MobileHandset> search(final MultiValueMap<String, Object> requestParams) {
        final Query orQuery = new Query();
        final Criteria andCriteria = new Criteria();
        final List<Criteria> andExpression = new ArrayList<>();

        final Map<String, Object> formattedParams = formattedParams(requestParams);
        checkParameterValidity(requestParams, formattedParams);
        //Iterate through parameters to build dynamic query
        for (Map.Entry<String, Object> param : formattedParams.entrySet()) {
            Criteria expression = new Criteria();
            final Class<?> type = Utils.getFieldDataType(param.getKey(), MobileHandset.class).get(0);
            final String value = String.valueOf(param.getValue());
            if (Number.class.isAssignableFrom(requireNonNull(type))) {
                expression.and(param.getKey()).is(parseInt(value));
            } else {
                expression.and(param.getKey()).regex(value, "i");
            }
            andExpression.add(expression);
        }
        if (!andExpression.isEmpty())
            orQuery.addCriteria(andCriteria.andOperator(andExpression.toArray(new Criteria[0])));

        return mongoTemplate.find(orQuery, MobileHandset.class);
    }

    /**
     * @param requestParams   input parameter from user
     * @param formattedParams with sub document name
     *                        Checks param validity in case all params are invalid ,if at least one of them are valid
     *                        then rest of them will be ignored
     */
    private void checkParameterValidity(MultiValueMap<String, Object> requestParams, Map<String, Object> formattedParams) {
        if (requestParams.size() != 0 && formattedParams.size() == 0)
            throw new InvalidRequestParameterException(requestParams.toString() + " are invalid parameters");
    }

}
