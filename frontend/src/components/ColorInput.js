// src/components/ColorInput.js
import React, { useState } from 'react'
import { useInput, useRecordContext } from 'ra-core'
import { TextInput } from 'react-admin'
import * as ReactColor from 'react-color'
import './ColorInput.css'

export const ColorField = ({ source }) => {
  const record = useRecordContext()

  return (
    <div style={{ display: 'flex' }}>
      <div
        style={{
          width: '20px',
          height: '20px',
          background: record[source],
          marginRight: '5px',
        }}
      />
    </div>
  )
}

export const ColorInput = (props) => {
  const [show, setShow] = useState(false)

  const {
    label,
    source,
    fullWidth,
    options,
    picker = 'Chrome',
    validate,
  } = props

  const {
    field,
    fieldState: { isTouched, error },
    isRequired,
  } = useInput({
    ...props,
    validate,
  })

  const handleOpen = () => setShow(true)
  const handleClose = () => setShow(false)
  const handleChange = ({ hex }) => {
    field.onChange(hex)
  }

  const Picker = ReactColor[`${picker}Picker`]

  return (
    <div style={{ display: 'flex' }}>
      <TextInput
        {...field}
        fullWidth={fullWidth}
        source={source}
        onFocus={handleOpen}
        isRequired={isRequired}
        label={label}
        error={!!(isTouched && error)}
      />
      {show ? (
        <div className="ColorInput-popup">
          <div className="ColorInput-cover" onClick={handleClose} />
          <Picker {...options} color={field.value} onChange={handleChange} />
        </div>
      ) : null}
      <div
        style={{
          width: '20px',
          height: '40px',
          background: field.value ? field.value : "#FFFFFF",
          border: "1px solid #bbb",
          marginRight: '5px',
          marginLeft: '-1px'
        }}
      />
    </div>
  )
}
