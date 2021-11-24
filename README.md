# Sensor data reader

Reads sensor data provided in csv files.

## Running instructions

```sbt "run /path/to/csv/directory"```

## Testing

```sbt test```

## More elaborate task solution description

The solution uses fs2 to read the files. Reads are done in chunks of 4 mb. I think that the `SensorDataCollector` could
be further improved with:

- better error handling. Currently, the error where a file is missing columns (eg. in the middle) will not be caught. I
  would need some more time to figure it out right. For now, it is assumed that the csv files are correct.
- probably the performance of reading the files could be tweaked even more (bigger chunks, aggregating partial results
  using `chunkN` on `Row` instances and then process partially aggregated results rather than single rows).

### Ideas

To be considered if [newtype](https://github.com/estatico/scala-newtype) macros should be used to further reduce the
runtime overhead (how big gain would that be? Is it worth it? To be checked)
