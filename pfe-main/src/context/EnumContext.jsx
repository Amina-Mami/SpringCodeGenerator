import React, { createContext, useState } from "react";

export const EntityContext = createContext();

export const EntityProvider = ({ children }) => {
  const [enumValues, setEnumValues] = useState([]);

  const updateEnumValues = (index, values) => {
    setEnumValues((prevEnumValues) => {
      const updatedValues = [...prevEnumValues];
      updatedValues[index] = values;
      return updatedValues;
    });
  };

  return (
    <EntityContext.Provider value={{ enumValues, updateEnumValues }}>
      {children}
    </EntityContext.Provider>
  );
};
