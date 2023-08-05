package cz.jakvitov.wes.utils.user;

import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.UserEntity;

import java.util.*;

public class UserUtils {

    public static Map<CityEntity, Set<UserEntity>> sortUsersByCity(List<UserEntity> userEntities){
        Map<CityEntity, Set<UserEntity>> result = new HashMap<>();
        for (UserEntity user : userEntities){
            if (result.containsKey(user.getCity())){
                result.get(user.getCity()).add(user);
            }
            else {
                Set<UserEntity> cityUsers = new HashSet<>();
                cityUsers.add(user);
                result.put(user.getCity(), cityUsers);
            }
        }
        return result;
    }

}
