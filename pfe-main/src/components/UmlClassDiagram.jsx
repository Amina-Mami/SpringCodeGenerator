import React from "react";
import ReactFlow from "react-flow-renderer";

const UmlClassDiagram = ({ elements }) => {
  return (
    <ReactFlow
      elements={elements}
      style={{ width: "100%", height: "400px", border: "1px solid #ccc" }}
    />
  );
};

export default UmlClassDiagram;
