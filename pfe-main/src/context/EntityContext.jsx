// import React, { createContext, useState } from "react";

// export const EntityContext = createContext();

// export const EntityProvider = ({ children }) => {
//   const [entities, setEntities] = useState([]);
//   const [relationships, setRelationships] = useState([]);

//   return (
//     <EntityContext.Provider
//       value={{ entities, setEntities, relationships, setRelationships }}
//     >
//       {children}
//     </EntityContext.Provider>
//   );
// };

import React, { createContext, useState } from "react";

export const EntityContext = createContext();

export const EntityProvider = ({ children }) => {
  const [entities, setEntities] = useState([]);

  return (
    <EntityContext.Provider value={{ entities, setEntities }}>
      {children}
    </EntityContext.Provider>
  );
};
