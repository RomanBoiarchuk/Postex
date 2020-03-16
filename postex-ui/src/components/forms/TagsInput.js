import {Badge, Button, FormControl, InputGroup} from "react-bootstrap";
import {Field} from "formik";
import React from "react";

export function TagsInput(props) {
    let arrayHelpers = props.arrayHelpers;
    let values = props.values;
    return (
        <div>
            <InputGroup className="mb-3">
                <FormControl
                    id="nextTag"
                    placeholder="#"
                />
                <InputGroup.Append>
                    <Button variant="outline-primary"
                            onClick={() => {
                                let input = document.getElementById("nextTag");
                                if (input.value.trim() !== '') {
                                    arrayHelpers.push(input.value);
                                    input.value = '';
                                }
                            }}
                    >Add a tag</Button>
                </InputGroup.Append>
            </InputGroup>
            {values.tags && values.tags.length > 0 &&
            (
                values.tags.map((tag, index) =>
                    (
                        <span className="mt-2 mr-2 inline-block" key={index}>
                                <Field name={`tags.${index}`} hidden/>
                                <Button variant="outline-primary"
                                        onClick={() => arrayHelpers.remove(index)}
                                > #{tag} <Badge variant="outline-primary">X</Badge>
                                </Button>
                            </span>
                    ))
            )}
        </div>
    );
}
