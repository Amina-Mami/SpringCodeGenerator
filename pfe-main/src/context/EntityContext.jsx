import { createContext, useState } from "react";

export const EntityContext = createContext();

export const EntityProvider = ({ children }) => {
  const [entities, setEntities] = useState([]);
  const [userId, setUserId] = useState(null);
  const [enumValues, setEnumValues] = useState([]);

  const updateEnumValues = (index, values) => {
    setEnumValues((prevEnumValues) => {
      const updatedValues = [...prevEnumValues];
      updatedValues[index] = values;
      return updatedValues;
    });
  };

  return (
    <EntityContext.Provider
      value={{
        entities,
        setEntities,
        userId,
        setUserId,
        enumValues,
        setEnumValues,
        updateEnumValues,
      }}
    >
      {children}
    </EntityContext.Provider>
  );
};
