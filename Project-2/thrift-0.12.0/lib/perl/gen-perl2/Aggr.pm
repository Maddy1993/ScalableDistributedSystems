#
# Autogenerated by Thrift Compiler (0.12.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
use 5.10.0;
use strict;
use warnings;
use Thrift::Exception;
use Thrift::MessageType;
use Thrift::Type;

use Types;


# HELPER FUNCTIONS AND STRUCTURES

package Aggr_addValue_args;
use base qw(Class::Accessor);
Aggr_addValue_args->mk_accessors( qw( value ) );

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  $self->{value} = undef;
  if (UNIVERSAL::isa($vals,'HASH')) {
    if (defined $vals->{value}) {
      $self->{value} = $vals->{value};
    }
  }
  return bless ($self, $classname);
}

sub getName {
  return 'Aggr_addValue_args';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1)
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == Thrift::TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
      /^1$/ && do{      if ($ftype == Thrift::TType::I32) {
        $xfer += $input->readI32(\$self->{value});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Aggr_addValue_args');
  if (defined $self->{value}) {
    $xfer += $output->writeFieldBegin('value', Thrift::TType::I32, 1);
    $xfer += $output->writeI32($self->{value});
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package Aggr_addValue_result;
use base qw(Class::Accessor);

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  return bless ($self, $classname);
}

sub getName {
  return 'Aggr_addValue_result';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1)
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == Thrift::TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Aggr_addValue_result');
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package Aggr_getValues_args;
use base qw(Class::Accessor);

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  return bless ($self, $classname);
}

sub getName {
  return 'Aggr_getValues_args';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1)
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == Thrift::TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Aggr_getValues_args');
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package Aggr_getValues_result;
use base qw(Class::Accessor);
Aggr_getValues_result->mk_accessors( qw( success ) );

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  $self->{success} = undef;
  $self->{err} = undef;
  if (UNIVERSAL::isa($vals,'HASH')) {
    if (defined $vals->{success}) {
      $self->{success} = $vals->{success};
    }
    if (defined $vals->{err}) {
      $self->{err} = $vals->{err};
    }
  }
  return bless ($self, $classname);
}

sub getName {
  return 'Aggr_getValues_result';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1)
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == Thrift::TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
      /^0$/ && do{      if ($ftype == Thrift::TType::LIST) {
        {
          my $_size0 = 0;
          $self->{success} = [];
          my $_etype3 = 0;
          $xfer += $input->readListBegin(\$_etype3, \$_size0);
          for (my $_i4 = 0; $_i4 < $_size0; ++$_i4)
          {
            my $elem5 = undef;
            $xfer += $input->readI32(\$elem5);
            push(@{$self->{success}},$elem5);
          }
          $xfer += $input->readListEnd();
        }
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^1$/ && do{      if ($ftype == Thrift::TType::STRUCT) {
        $self->{err} = Error->new();
        $xfer += $self->{err}->read($input);
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Aggr_getValues_result');
  if (defined $self->{success}) {
    $xfer += $output->writeFieldBegin('success', Thrift::TType::LIST, 0);
    {
      $xfer += $output->writeListBegin(Thrift::TType::I32, scalar(@{$self->{success}}));
      {
        foreach my $iter6 (@{$self->{success}}) 
        {
          $xfer += $output->writeI32($iter6);
        }
      }
      $xfer += $output->writeListEnd();
    }
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{err}) {
    $xfer += $output->writeFieldBegin('err', Thrift::TType::STRUCT, 1);
    $xfer += $self->{err}->write($output);
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package AggrIf;

use strict;


sub addValue{
  my $self = shift;
  my $value = shift;

  die 'implement interface';
}

sub getValues{
  my $self = shift;

  die 'implement interface';
}

package AggrRest;

use strict;


sub new {
  my ($classname, $impl) = @_;
  my $self     ={ impl => $impl };

  return bless($self,$classname);
}

sub addValue{
  my ($self, $request) = @_;

  my $value = ($request->{'value'}) ? $request->{'value'} : undef;
  return $self->{impl}->addValue($value);
}

sub getValues{
  my ($self, $request) = @_;

  return $self->{impl}->getValues();
}

package AggrClient;


use base qw(AggrIf);
sub new {
  my ($classname, $input, $output) = @_;
  my $self      = {};
  $self->{input}  = $input;
  $self->{output} = defined $output ? $output : $input;
  $self->{seqid}  = 0;
  return bless($self,$classname);
}

sub addValue{
  my $self = shift;
  my $value = shift;

    $self->send_addValue($value);
  $self->recv_addValue();
}

sub send_addValue{
  my $self = shift;
  my $value = shift;

  $self->{output}->writeMessageBegin('addValue', Thrift::TMessageType::CALL, $self->{seqid});
  my $args = Aggr_addValue_args->new();
  $args->{value} = $value;
  $args->write($self->{output});
  $self->{output}->writeMessageEnd();
  $self->{output}->getTransport()->flush();
}

sub recv_addValue{
  my $self = shift;

  my $rseqid = 0;
  my $fname;
  my $mtype = 0;

  $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
  if ($mtype == Thrift::TMessageType::EXCEPTION) {
    my $x = Thrift::TApplicationException->new();
    $x->read($self->{input});
    $self->{input}->readMessageEnd();
    die $x;
  }
  my $result = Aggr_addValue_result->new();
  $result->read($self->{input});
  $self->{input}->readMessageEnd();

  return;
}
sub getValues{
  my $self = shift;

    $self->send_getValues();
  return $self->recv_getValues();
}

sub send_getValues{
  my $self = shift;

  $self->{output}->writeMessageBegin('getValues', Thrift::TMessageType::CALL, $self->{seqid});
  my $args = Aggr_getValues_args->new();
  $args->write($self->{output});
  $self->{output}->writeMessageEnd();
  $self->{output}->getTransport()->flush();
}

sub recv_getValues{
  my $self = shift;

  my $rseqid = 0;
  my $fname;
  my $mtype = 0;

  $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
  if ($mtype == Thrift::TMessageType::EXCEPTION) {
    my $x = Thrift::TApplicationException->new();
    $x->read($self->{input});
    $self->{input}->readMessageEnd();
    die $x;
  }
  my $result = Aggr_getValues_result->new();
  $result->read($self->{input});
  $self->{input}->readMessageEnd();

  if (defined $result->{success} ) {
    return $result->{success};
  }
  if (defined $result->{err}) {
    die $result->{err};
  }
  die "getValues failed: unknown result";
}
package AggrProcessor;

use strict;


sub new {
    my ($classname, $handler) = @_;
    my $self      = {};
    $self->{handler} = $handler;
    return bless ($self, $classname);
}

sub process {
    my ($self, $input, $output) = @_;
    my $rseqid = 0;
    my $fname  = undef;
    my $mtype  = 0;

    $input->readMessageBegin(\$fname, \$mtype, \$rseqid);
    my $methodname = 'process_'.$fname;
    if (!$self->can($methodname)) {
      $input->skip(Thrift::TType::STRUCT);
      $input->readMessageEnd();
      my $x = Thrift::TApplicationException->new('Function '.$fname.' not implemented.', Thrift::TApplicationException::UNKNOWN_METHOD);
      $output->writeMessageBegin($fname, Thrift::TMessageType::EXCEPTION, $rseqid);
      $x->write($output);
      $output->writeMessageEnd();
      $output->getTransport()->flush();
      return;
    }
    $self->$methodname($rseqid, $input, $output);
    return 1;
}

sub process_addValue {
    my ($self, $seqid, $input, $output) = @_;
    my $args = Aggr_addValue_args->new();
    $args->read($input);
    $input->readMessageEnd();
    my $result = Aggr_addValue_result->new();
    $self->{handler}->addValue($args->value);
    $output->writeMessageBegin('addValue', Thrift::TMessageType::REPLY, $seqid);
    $result->write($output);
    $output->writeMessageEnd();
    $output->getTransport()->flush();
}

sub process_getValues {
    my ($self, $seqid, $input, $output) = @_;
    my $args = Aggr_getValues_args->new();
    $args->read($input);
    $input->readMessageEnd();
    my $result = Aggr_getValues_result->new();
    eval {
      $result->{success} = $self->{handler}->getValues();
    }; if( UNIVERSAL::isa($@,'Error') ){ 
      $result->{err} = $@;
      $@ = undef;
    }
    if ($@) {
      $@ =~ s/^\s+|\s+$//g;
      my $err = Thrift::TApplicationException->new("Unexpected Exception: " . $@, Thrift::TApplicationException::INTERNAL_ERROR);
      $output->writeMessageBegin('getValues', Thrift::TMessageType::EXCEPTION, $seqid);
      $err->write($output);
      $output->writeMessageEnd();
      $output->getTransport()->flush();
      $@ = undef;
      return;
    }
    $output->writeMessageBegin('getValues', Thrift::TMessageType::REPLY, $seqid);
    $result->write($output);
    $output->writeMessageEnd();
    $output->getTransport()->flush();
}

1;