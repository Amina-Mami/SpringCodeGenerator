import React, { createContext, useState } from "react";

export const EnumContext = createContext();

export const EnumProvider = ({ children }) => {
  const [enumValues, setEnumValues] = useState([]); // Ensure enumValues is initialized properly

  return (
    <EnumContext.Provider value={{ enumValues, setEnumValues }}>
      {children}
    </EnumContext.Provider>
  );
};
